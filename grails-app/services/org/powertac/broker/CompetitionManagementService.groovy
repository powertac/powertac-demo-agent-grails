/*
 * Copyright (c) 2011 by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.Instant
import org.powertac.broker.api.GameStateType
import org.powertac.broker.interfaces.MessageListenerWithAutoRegistration
import org.powertac.broker.interfaces.TimeslotPhaseProcessorWithAutoRegistration
import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.common.TimeService
import org.powertac.common.TimedAction
import org.quartz.SimpleTrigger
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.powertac.common.msg.*

class CompetitionManagementService implements MessageListenerWithAutoRegistration, ApplicationContextAware
{
  static transactional = false

  Competition competition // convenience var, invalid across sessions
  String competitionId
  boolean running

  long timeslotMillis
  long startTime

  def timeslotPhaseService
//  def logService

  def quartzScheduler
  def clockDriveJob
  def timeService // inject simulation time service dependency

  def jmsConnectionFactory
  def jmsManagementService
  def messageReceiver
  def gameStateService
  def pauseActionStateService

  def shoutRequestService


  def applicationContext
  def grailsApplication
  def dataSource

  String dumpFilePrefix = (ConfigurationHolder.config.powertac?.dumpFilePrefix) ?: "logs/PowerTAC-dump-"

  def initialize (loginResponseCmd) {
    GameState.withTransaction {
      GameState.initialize()
      PauseActionState.initialize()
    }

    // Generate broker URL and set it. Connection will be established automatically.
    setBrokerUrl(loginResponseCmd.serverAddress)
	jmsManagementService.initialize(loginResponseCmd.serverQueueName)
    jmsManagementService.registerBrokerMessageListener(loginResponseCmd.brokerQueueName, messageReceiver)

    def msgClassMap = applicationContext.getBeansOfType(MessageListenerWithAutoRegistration)
    msgClassMap.each { clazz, receiver ->
      def logMsg = "registering ${clazz} with"
      def msgs = receiver.messages
      msgs?.each { message ->
        jmsManagementService.register(message, receiver)
      }
      log.info("${logMsg} ${msgs.collect { it.simpleName }?.join(',')}")
    }

    def phaseClassMap = applicationContext.getBeansOfType(TimeslotPhaseProcessorWithAutoRegistration)
    phaseClassMap.each { clazz, processor ->
      def logMsg = "registering ${clazz} with TimeSlotPhaseService for phase"
      def phases = processor.phases
      phases?.each { phase ->
        timeslotPhaseService.registerTimeslotPhase(processor, phase)
      }
      log.info("${logMsg} ${phases?.join(',')}")
    }

	  BrokerAuthentication ba = new BrokerAuthentication()
	  ba.setBroker(new Broker(ConfigurationHolder.config.powertac.username))
	  jmsManagementService.send(ba)
  }

  def uninitialize() {
    jmsManagementService.unregisterBrokerMessageListener(messageReceiver)

    def msgClassMap = applicationContext.getBeansOfType(MessageListenerWithAutoRegistration)
    msgClassMap.each { clazz, receiver ->
      def logMsg = "unregistering ${clazz} with"
      def msgs = receiver.messages
      msgs?.each { message ->
        jmsManagementService.unregister(message, receiver)
      }
      log.info("${logMsg} ${msgs.collect { it.simpleName }?.join(',')}")
    }

    def phaseClassMap = applicationContext.getBeansOfType(TimeslotPhaseProcessorWithAutoRegistration)
    phaseClassMap.each { clazz, processor ->
      def logMsg = "unregistering ${clazz} with TimeSlotPhaseService"
      def phases = processor.phases
      phases?.each { phase ->
        timeslotPhaseService.unregisterTimeslotPhase(processor, phase)
      }
      log.info("${logMsg} ${phases?.join(',')}")
    }
    setBrokerUrl('')
  }


  def isConnected () {
    def brokerUrl = getBrokerUrl()
    brokerUrl != null && !brokerUrl.empty
  }

  def getBrokerUrl () {
    jmsConnectionFactory.targetConnectionFactory.brokerURL
  }

  def setBrokerUrl (url) {
    def completeUrl = url
    if (url != null && !url.empty) {
      completeUrl += ConfigurationHolder.config.powertac.brokerUrlOpts
    }
    log.debug("setBrokerUrl: completeUrl:${completeUrl}")
    jmsConnectionFactory.targetConnectionFactory.brokerURL = completeUrl
  }

  /**
   * Starts the simulation.
   */
  void start (long start) {
    log.debug("start - start")

//    logService.start()

    setTimeParameters()

    // Start up the clock at the correct time
    startTime = start
    timeService.start = start
    timeService.updateTime()

    // Set final paramaters
    running = true

    if (competition) {
      // schedule first task
      scheduleStep(0)
      startTimer(start)
    }

    log.debug("start - end")
  }

  def startTimer (start) {
    def trigger = quartzScheduler.getTrigger('default', 'default')
    log.debug("startTimer - [jobName:${trigger?.jobName},jobGroup:${trigger?.jobGroup}]")

    // Only for resume
    if (startTime < start) {
      long originalNextTick = computeNextTickTime()
      long actualNextTick = new Date().time
      startTime += actualNextTick - originalNextTick;
      timeService.setStart(start);
    }

    if (trigger) {
      trigger.repeatInterval = timeslotMillis / competition.simulationRate
      trigger.repeatCount = SimpleTrigger.REPEAT_INDEFINITELY
      trigger.startTime = new Date(start)
      quartzScheduler.rescheduleJob(trigger.name,
          trigger.group,
          trigger)
    }
  }


  private long computeNextTickTime ()
  {
    long simTime = timeService.currentTime.millis;
    long nextSimTime = simTime + timeService.modulo;
    long nextTick = startTime + (nextSimTime - timeService.base) / timeService.rate
    return nextTick;
  }


  def pauseTimer () {
    def trigger = quartzScheduler.getTrigger('default', 'default')
    if (trigger) {
      quartzScheduler.pauseJob(trigger.jobName, trigger.jobGroup)
    }
  }

  /**
   * Schedules a step of the simulation
   */
  void scheduleStep (long offset) {
    timeService.addAction(new Instant(timeService.currentTime.millis + offset),
			new TimedAction() {
				void perform(Instant instant) {
					this.step()
				}
			});
  }

  /**
   * Runs a step of the simulation
   */
  void step () {
    log.debug("step - start")
    if (!running) {
      log.info("Stop simulation")
      shutDown()
    } else {
      def time = timeService.currentTime
      log.info "step at $time"

      timeslotPhaseService.process(time)
      scheduleStep(timeslotMillis)
    }
    log.debug("step - end")
  }

  /**
   * Stops the simulation.
   */
  void stop () {
    running = false
  }

  /**
   * Shuts down the simulation and cleans up
   */
  void shutDown () {
    running = false
    pauseTimer()

    // disconnect
    uninitialize()

  }

  //--------- local methods -------------

  // set simulation time parameters, making sure that simulationStartTime
  // is still sufficiently in the future.
  void setTimeParameters () {
    timeService.base = competition.simulationBaseTime.millis
    timeService.currentTime = competition.simulationBaseTime
    long rate = competition.simulationRate
    long rem = rate % competition.timeslotLength
    if (rem > 0) {
      long mult = competition.simulationRate / competition.timeslotLength
      log.warn "Simulation rate ${rate} not a multiple of ${competition.timeslotLength}; adjust to ${(mult + 1) * competition.timeslotLength}"
      rate = (mult + 1) * competition.timeslotLength
    }
    timeService.rate = rate
    timeService.modulo = competition.timeslotLength * TimeService.MINUTE
  }

  def getMessages () {
    [Competition, SimStart, TimeslotUpdate, SimEnd, SimPause, SimResume]
  }

  def onMessage (Competition competition) {
    log.debug("onMessage(Competition) - start")

    this.competition = competition;
    this.competitionId = competition.id
    this.timeslotMillis = competition.timeslotLength * TimeService.MINUTE

    log.debug("onMessage(Competition) - end")
  }

  def onMessage (SimStart simStart) {
    log.debug("onMessage(SimStart) - start")

    gameStateService.setState(GameStateType.RUNNING)
    pauseActionStateService.updateState()

    this.start(simStart.start.millis)

    log.debug("onMessage(SimStart) - end")
  }

  def onMessage (SimEnd simEnd) {
    log.debug("onMessage(SimEnd) - start")

    gameStateService.setState(GameStateType.STOPPED)
    pauseActionStateService.updateState()

    this.stop()

    log.debug("onMessage(SimEnd) - end")
  }

  def onMessage (TimeslotUpdate slotUpdate) {
    log.debug("onMessage(TimeslotUpdate) - start")

    log.debug("onMessage(TimeslotUpdate) - end")
  }

  def onMessage (SimPause sp) {
    log.debug("onMessage(SimPause) - start")

    gameStateService.setState(GameStateType.PAUSED)
    pauseActionStateService.updateState()

    pauseTimer()

    log.debug("onMessage(SimPause) - end : [state:${gameStateService.state}")
  }

  def onMessage (SimResume sr) {
    log.debug("onMessage(SimResume) - start")

    gameStateService.setState(GameStateType.RUNNING)
    pauseActionStateService.updateState()

    startTimer(sr.start.millis)
    log.debug("onMessage(SimResume) - end : [state:${gameStateService.state}")
  }

  void setApplicationContext (ApplicationContext applicationContext) {
    this.applicationContext = applicationContext
  }
}