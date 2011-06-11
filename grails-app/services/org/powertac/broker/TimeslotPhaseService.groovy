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
  }

  void unregisterTimeslotPhase (TimeslotPhaseProcessor thing, int phase) {
    if (phase <= 0 || phase > timeslotPhaseCount) {
      def msg = "phase ${phase} out of range (1..${timeslotPhaseCount})"
      throw new IllegalArgumentException(msg)
    } else {
      phaseRegistrations[phase - 1].remove(thing)
    }
  }

  void unregisterTimeslotPhase (TimeslotPhaseProcessor thing) {
    phaseRegistrations.each { fnList ->
      fnList.remove(thing)
    }
  }

  protected void process (time) {
    phaseRegistrations.eachWithIndex { fnList, index ->
      log.info "activate phase ${index + 1}: ${fnList}"
      fnList*.activate(time, index + 1)
    }
  }
}
