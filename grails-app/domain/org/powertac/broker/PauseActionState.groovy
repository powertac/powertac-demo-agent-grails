package org.powertac.broker

import org.powertac.broker.api.PauseActionType

class PauseActionState
{
  static final long SINGLETON_ID = 1

  PauseActionType state = PauseActionType.NONE

  static constraints = {
  }

  static initialize() {
    PauseActionState currentState = PauseActionState.get(PauseActionState.SINGLETON_ID)
    if (!currentState) {
      currentState = new PauseActionState()
    }
    currentState.state = PauseActionType.NONE
    currentState.save ()
  }
}
