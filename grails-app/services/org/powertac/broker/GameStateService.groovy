package org.powertac.broker

import org.powertac.broker.api.GameStateType

class GameStateService
{
  static transactional = true

  def getState () {
    GameStateType currentStateType = GameStateType.STOPPED
    GameState currentState = GameState.get(GameState.SINGLETON_ID)
    if (currentState) {
      currentStateType = currentState.state
    }

    return currentStateType
  }

  def setState (GameStateType stateType) {
    GameState currentState = GameState.get(GameState.SINGLETON_ID)
    if (currentState) {
      currentState.state = stateType
    } else {
      log.error("setState - GameState has not been initialized!")
    }
  }
}
