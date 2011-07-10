package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.Instant
import org.powertac.common.Broker
import org.powertac.common.Shout
import org.powertac.common.Competition
import org.powertac.broker.interfaces.TimeslotPhaseProcessorWithAutoRegistration
import org.powertac.common.Timeslot
import org.powertac.common.TimeService

class ShoutRequestService implements TimeslotPhaseProcessorWithAutoRegistration
{
  static transactional = true

  def jmsManagementService

  def getPhases() {
    [1]
  }

  void activate (Instant time, int phaseNumber) {
    Timeslot timeslot = getTimeSlot(time)
    if (timeslot) {
      def shouts = ShoutRequest.findAllActiveAtTimeslot(timeslot.serialNumber).list()
      shouts?.each { shoutRequest ->
        log.debug("activate -    shout: ${shoutRequest}")
        def shout = new Shout(shoutRequest.properties)
        shout.broker = Broker.findByUsername(ConfigurationHolder.config.powertac.username)
        shout.timeslot = getTimeSlot(time)
        jmsManagementService.send(shout)
      }
    }
  }

  def getTimeSlot(time) {
    def competition = Competition.currentCompetition()
    def timeslot = null
    if (competition) {
      def startTime = competition.simulationBaseTime
      int numTimeslots = (time.millis - startTime.millis) / (competition.timeslotLength * TimeService.MINUTE)
      log.debug("startTime: ${startTime}, currentTime: ${time}, numTimeslots: ${numTimeslots}")
      timeslot = new Timeslot(serialNumber: numTimeslots + 1)
    }
    return timeslot
  }

}

