package org.powertac.broker
import javax.jms.MessageListener
import javax.jms.Message
import org.apache.log4j.Logger

/**
 * Server Message Listener
 */
class ServerMessageListener implements MessageListener {
  def log = Logger.getLogger(ServerMessageListener)
  def void onMessage(Message message) {
    log.info "Received from server: ${message}"
  }
}
