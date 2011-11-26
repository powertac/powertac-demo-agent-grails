package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.powertac.broker.api.PauseActionType
import org.powertac.common.Broker
import org.powertac.common.msg.PauseRequest
import org.powertac.common.msg.PauseRelease

class GameStatusController
{
  def gameStateService
  def pauseActionStateService
  def cashPositionService
  def jmsManagementService
  def connectionService

	def index = {
		redirect(controller: 'status')
	}

  def getGameState = {
    render gameStateService.state
  }

  def getPauseActionState = {
    render pauseActionStateService.state
  }

  def getCashBalance = {
    render sprintf('\$%.2f', cashPositionService.getCashPosition(ConfigurationHolder.config.powertac.username))
  }

  def pauseAction = {
    def msg
    if (connectionService.isConnected()) {
      def request
      def broker = new Broker(ConfigurationHolder.config.powertac.username)
      def pauseStateType = pauseActionStateService.state
      def newPauseStateType = PauseActionType.NONE

      if (pauseStateType == PauseActionType.NONE
          || pauseStateType == PauseActionType.RESUME_REQUESTED) {
        request = new PauseRequest(broker)
        newPauseStateType = PauseActionType.PAUSE_REQUESTED
      } else {
        request = new PauseRelease(broker)
        newPauseStateType = PauseActionType.RESUME_REQUESTED
      }

      pauseActionStateService.setState(newPauseStateType)
      jmsManagementService.send(request)
      msg = "${request.class.simpleName} is submitted"
    } else {
      msg = "Not connected to server"
    }

    render msg
  }
}


