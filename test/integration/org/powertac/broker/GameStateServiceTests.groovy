package org.powertac.broker

import grails.test.*
import org.powertac.broker.api.GameStateType

class GameStateServiceTests extends GroovyTestCase
{
  def gameStateService

  protected void setUp () {
    super.setUp()
  }

  protected void tearDown () {
    super.tearDown()
  }

  void testStopped () {
    GameState.get(GameState.SINGLETON_ID)?.delete()
    assertEquals("stopped state", GameStateType.STOPPED, gameStateService.getState())
  }

  void testRunning() {
    GameState currentState = GameState.get(GameState.SINGLETON_ID)
    assertNotNull(currentState)
    currentState.state = GameStateType.RUNNING

    assertEquals("running state", GameStateType.RUNNING, gameStateService.getState())
  }

  void testPaused() {
    GameState currentState = GameState.get(GameState.SINGLETON_ID)
    assertNotNull(currentState)
    currentState.state = GameStateType.PAUSED

    assertEquals("paused state", GameStateType.PAUSED, gameStateService.getState())
  }
}
