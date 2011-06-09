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
import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.common.TimeService
import org.powertac.common.msg.SimEnd
import org.powertac.common.msg.SimStart
import org.powertac.common.msg.TimeslotUpdate
import org.quartz.SimpleTrigger
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class CompetitionManagementService implements MessageListener, ApplicationContextAware
{
  static transactional = true

  Competition competition // convenience var, invalid across sessions
  String competitionId
  boolean running

  long timeslotMillis
  long startTime

  def timeslotPhaseService

  def quartzScheduler
  def clockDriveJob
  def timeService // inject simulation time service dependency

  def jmsConnectionFactory
  def jmsManagementService
  def messageReceiver

  def applicationContext

  def initialize (loginResponseCmd) {
    // Generate broker URL and set it. Connection will be established automatically.
    setBrokerUrl(loginResponseCmd.serverAddress)
    jmsManagementService.registerBrokerMessageListener(loginResponseCmd.queueName, messageReceiver)

    def classMap = applicationContext.getBeansOfType(MessageListener)
    classMap.each { clazz, receiver ->
      def logMsg = "registering ${clazz} with"
      def msgs = receiver.messages
      msgs?.each { message ->
        jmsManagementService.register(message, receiver)
      }
      log.info("${logMsg} ${msgs.collect { it.simpleName }?.join(',')}")
    }
  }

  def isConnected () {
    def brokerUrl = getBrokerUrl()
    brokerUrl != null && !brokerUrl.isEmpty()
  }

  def getBrokerUrl () {
    jmsConnectionFactory.targetConnectionFactory.brokerURL
  }

  def setBrokerUrl (url) {
    def completeUrl = url + ConfigurationHolder.config.powertac.brokerUrlOpts
    log.debug("setBrokerUrl: completeUrl:${completeUrl}")
    jmsConnectionFactory.targetConnectionFactory.brokerURL = completeUrl
  }

  /**
   * Starts the simulation.
   */
  void start (long start) {
    log.debug("start - start")
    quartzScheduler.start()

    setTimeParameters()

    // Start up the clock at the correct time
    timeService.start = start
    timeService.updateTime()

    // Set final paramaters
    running = true

    def beginTime = System.nanoTime()
    def beginRescheduleTime
    def endTime
    def nextFireTime

    if (competition) {
      // schedule first task
      scheduleStep(0)

      beginRescheduleTime = System.nanoTime()
      def repeatJobTrigger = quartzScheduler.getTrigger('default', 'default')
      repeatJobTrigger.repeatInterval = timeslotMillis / competition.simulationRate
      repeatJobTrigger.repeatCount = SimpleTrigger.REPEAT_INDEFINITELY
      repeatJobTrigger.startTime = new Date(start)
      nextFireTime = quartzScheduler.rescheduleJob(repeatJobTrigger.name,
          repeatJobTrigger.group,
          repeatJobTrigger)
      endTime = System.nanoTime()
    }

    log.debug("start - time for db search: ${beginRescheduleTime - beginTime}")
    log.debug("start - time for reschedule: ${endTime - beginRescheduleTime}")
    log.debug("start - total time: ${endTime - beginTime}")
    log.debug("start - nextFireTime: ${nextFireTime}")

    log.debug("start - end")
  }

  /**
   * Schedules a step of the simulation
   */
  void scheduleStep (long offset) {
    timeService.addAction(new Instant(timeService.currentTime.millis + offset),
        { this.step() })
  }

  /**
   * Runs a step of the simulation
   */
  void step () {
    if (!running) {
      log.info("Stop simulation")
      shutDown()
    }

    def time = timeService.currentTime
    log.info "step at $time"

    timeslotPhaseService.process(time)
    scheduleStep(timeslotMillis)
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
    quartzScheduler.shutdown()
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
    [Competition, SimStart, TimeslotUpdate, SimEnd]
  }

  def onMessage (Competition competition) {
    log.debug("onMessage(Competition) - start")

    competition.brokers?.each {
      log.debug("onMessage(Competition) - populate broker: ${it}")
      def broker = new Broker(username: it, enabled: true)
      broker.save()
    }

    log.debug("onMessage(Competition) - saving competition ${competition}:${competition.save() ? 'successful' : competition.errors}")

    this.competition = competition;
    this.competitionId = competition.id
    this.timeslotMillis = competition.timeslotLength * TimeService.MINUTE

    log.debug("onMessage(Competition) - end")
  }

  def onMessage (SimStart simStart) {
    log.debug("onMessage(SimStart) - start")
    log.debug("Saving simStart - start @ ${simStart.start}")

    simStart.save()

    log.debug("onMessage(SimStart) - this: ${this}")
    this.start(simStart.start.millis)

    log.debug("onMessage(SimStart) - end")
  }

  def onMessage (SimEnd simEnd) {
    shutDown()
  }

  def onMessage (TimeslotUpdate slotUpdate) {
    log.debug("onMessage(TimeslotUpdate) - start")

    log.debug("onMessage(TimeslotUpdate) - received TimeslotUpdate: ${slotUpdate.id}")

    def newEnableds = []
    slotUpdate.enabled?.each {
      it.id = it.serialNumber
      it.enabled = true
      it.endInstant = it.startInstant + timeslotMillis
      log.debug("onMessage(TimeslotUpdate) -    saving enabled timeslot ${it.id}: ${(newEnableds << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.enabled = newEnableds

    def newDisables = []
    slotUpdate.disabled?.each {
      it.id = it.serialNumber
      it.enabled = false
      it.endInstant = it.startInstant // FIXME
      log.debug("onMessage(TimeslotUpdate) -    saving disabled timeslot ${it.id}: ${(newDisables << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.disabled = newDisables

    log.debug("onMessage(TimeslotUpdate) - saving TimeslotUpdate ${slotUpdate.id}:${slotUpdate.save() ? 'successful' : slotUpdate.errors}")

    log.debug("onMessage(TimeslotUpdate) - end")
  }



  void setApplicationContext (ApplicationContext applicationContext) {
    this.applicationContext = applicationContext
  }
}