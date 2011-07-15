package org.powertac.broker

import org.powertac.common.interfaces.TimeslotPhaseProcessor
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class TimeslotPhaseService
{
  static transactional = false

  def phaseRegistrations
  def timeslotPhaseCount = (ConfigurationHolder.config.powertac?.numTimeslotPhases) ?: 3

  /**
   * Sign up for notifications
   */
  void registerTimeslotPhase (TimeslotPhaseProcessor thing, int phase) {
    log.debug("registerTimeslotPhase - start ${thing}:${phase}")
    if (phase <= 0 || phase > timeslotPhaseCount) {
      def msg = "phase ${phase} out of range (1..${timeslotPhaseCount})"
      throw new IllegalArgumentException(msg)
    }
    else {
      if (phaseRegistrations == null) {
        phaseRegistrations = new List[timeslotPhaseCount]
      }
      if (phaseRegistrations[phase - 1] == null) {
        phaseRegistrations[phase - 1] = [] // do we really have to do this?
      }
      phaseRegistrations[phase - 1].add(thing)
    }

    log.debug("registerTimeslotPhase - ")

    log.debug("registerTimeslotPhase - end")
  }

  boolean unregisterTimeslotPhase (TimeslotPhaseProcessor thing, int phase) {
    if (phase <= 0 || phase > timeslotPhaseCount) {
      def msg = "phase ${phase} out of range (1..${timeslotPhaseCount})"
      throw new IllegalArgumentException(msg)
    } else {
      phaseRegistrations[phase - 1].remove(thing)
    }
  }

  boolean unregisterTimeslotPhase (TimeslotPhaseProcessor thing) {
    boolean found = false
    phaseRegistrations.each { fnList ->
      found = fnList.remove(thing) ?: found
    }
  }

  protected void process (time) {
    log.debug("process - start [phaseRegistrations.count:${phaseRegistrations?.size()}]")
    phaseRegistrations.eachWithIndex { fnList, index ->
      log.info "activate phase ${index + 1}: ${fnList}"
      log.debug("process - [phase:${index},#fn:${fnList?.size()}]")
      fnList*.activate(time, index + 1)
    }
  }
}
