package org.powertac.broker

import org.powertac.broker.api.PauseActionType
import org.powertac.broker.api.GameStateType

class PauseActionStateService
{

  static transactional = true

  def gameStateService

  def getState () {
    PauseActionType currentStateType = PauseActionType.NONE
    PauseActionState currentState = PauseActionState.get(PauseActionState.SINGLETON_ID)
    if (currentState) {
      currentStateType = currentState.state
    }

    return currentStateType
  }

  def setState (PauseActionType stateType) {
    PauseActionState currentState = PauseActionState.get(PauseActionState.SINGLETON_ID)
    if (currentState) {
      currentState.state = stateType
    } else {
      log.error("setState - PauseActionState has not been initialized!")
    }
  }

  def updateState() {
    def currentStateType = getState()
    def newStateType = currentStateType
    def gameStateType = gameStateService.state

    if (gameStateType == GameStateType.PAUSED && currentStateType == PauseActionType.PAUSE_REQUESTED) {
      newStateType = PauseActionType.PAUSE_ACCEPTED
    } else if (gameStateType == GameStateType.RUNNING && currentStateType == PauseActionType.RESUME_REQUESTED) {
      newStateType = PauseActionType.NONE
    }

    if (currentStateType != newStateType) {
      setState(newStateType)
    }
  }
}
