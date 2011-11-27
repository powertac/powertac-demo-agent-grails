package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.joda.time.Instant
import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.broker.interfaces.TimeslotPhaseProcessorWithAutoRegistration
import org.powertac.common.Timeslot
import org.powertac.common.TimeService
import org.powertac.common.Order
import org.powertac.common.repo.TimeslotRepo

class ShoutRequestService implements TimeslotPhaseProcessorWithAutoRegistration
{
  static transactional = true

  def jmsManagementService
	def messagePersistenceManager
	TimeslotRepo timeslotRepo

  def getPhases() {
    [1]
  }

  void activate (Instant time, int phaseNumber) {
	  log.debug("activate(${time}, ${phaseNumber}) - start")
    Timeslot timeslot = getTimeSlot(time)
	  log.debug("activate - found timeslot ${timeslot}")
    if (timeslot) {
      def shouts = ShoutRequest.findAllActiveAtTimeslot(timeslot.serialNumber).list()
      shouts?.each { shoutRequest ->
        log.debug("activate - shout: ${shoutRequest}")
        def broker = new Broker(ConfigurationHolder.config.powertac.username)
        def ts = timeslot
	      def MWh = (shoutRequest.buySellIndicator == ShoutRequest.OrderType.BUY ? shoutRequest.quantity : shoutRequest.quantity * -1)
	      def limitPrice = shoutRequest.limitPrice
	      def order = new Order(broker, ts, MWh, limitPrice)
        jmsManagementService.send(order)
      }
    }
	  log.debug("activate(${time}, ${phaseNumber}) - end")
  }

  def getTimeSlot(time) {
	  def timeslot = null
    def competition = messagePersistenceManager.get(Competition.class)
	  log.debug("getTimeSlot - found competition ${competition}")
    if (competition) {
      def startTime = competition.simulationBaseTime
      int numTimeslots = (time.millis - startTime.millis) / (competition.timeslotLength * TimeService.MINUTE)
      log.debug("startTime: ${startTime}, currentTime: ${time}, numTimeslots: ${numTimeslots}")
      timeslot = timeslotRepo.findOrCreateBySerialNumber(numTimeslots)
    }
	  log.debug("getTimeSlot(${time}) - found timeslot ${timeslot}")
    return timeslot
  }
}

