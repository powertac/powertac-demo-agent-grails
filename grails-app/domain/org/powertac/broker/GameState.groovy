package org.powertac.broker

import org.powertac.broker.api.GameStateType

class GameState
{
  static final long SINGLETON_ID = 1

  long id = SINGLETON_ID
  GameStateType state = GameStateType.STOPPED

  static constraints = {
  }
}
