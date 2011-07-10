package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.powertac.common.command.SimResume
import org.powertac.common.msg.PauseRequest
import org.powertac.common.msg.PauseRelease
import org.powertac.common.Broker
import org.apache.activemq.transport.stomp.Stomp.Headers.Send

class GameStatusController
{
  def gameStateService
  def cashPositionService
  def jmsManagementService
  def connectionService

  def index = { }

  def getGameState = {
    render gameStateService.state
  }

  def getCashBalance = {
    render cashPositionService.getCashPosition(ConfigurationHolder.config.powertac.username)
  }

  def pauseAction = {
    def msg
    if (connectionService.isConnected()) {

      def request
      def broker = new Broker(username: ConfigurationHolder.config.powertac.username)
      if (params.request == 'RESUME') {
        request = new PauseRelease(broker: broker)
      } else {
        request = new PauseRequest(broker: broker)
      }

      jmsManagementService.send(request)

      msg = "${request.class.simpleName} is submitted"
    } else {
      msg = "Not connected to server"
    }

    render msg
  }
}


