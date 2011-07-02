package org.powertac.broker

class GameState
{
  static final long SINGLETON_ID = 1

  enum State {
    RUNNING,
    PAUSED,
    STOPPED
  }

  long id = SINGLETON_ID
  State state = State.STOPPED

  static constraints = {
  }
}
