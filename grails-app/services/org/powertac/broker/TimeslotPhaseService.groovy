package org.powertac.broker

import org.powertac.common.interfaces.TimeslotPhaseProcessor

class TimeslotPhaseService
{
  static transactional = false

  def phaseRegistrations

  /**
   * Sign up for notifications
   */
  void registerTimeslotPhase (TimeslotPhaseProcessor thing, int phase) {
    if (phase <= 0 || phase > timeslotPhaseCount) {
      log.error "phase ${phase} out of range (1..${timeslotPhaseCount})"
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
      log.error "phase ${phase} out of range (1..${timeslotPhaseCount})"
    } else {
      phaseRegistrations[phase - 1].remove(thing)
    }
  }

  void unregisterTimeslotPhase (TimeslotPhaseProcessor thing) {
    phaseRegistrations.each { fnList ->
      fnList.remove(thing)
    }
  }

  def process (time) {
    phaseRegistrations.eachWithIndex { fnList, index ->
      log.info "activate phase ${index + 1}: ${fnList}"
      fnList*.activate(time, index + 1)
    }
  }
}
