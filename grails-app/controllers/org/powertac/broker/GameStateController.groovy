package org.powertac.broker

class GameStateController
{
  def gameStateService

  def index = { }

  def getGameState = {
    render gameStateService.state
  }
}
