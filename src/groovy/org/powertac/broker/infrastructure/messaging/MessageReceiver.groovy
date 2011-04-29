
package org.powertac.broker.infrastructure.messaging

import javax.jms.MessageListener
import javax.jms.Message
import javax.jms.TextMessage

class MessageReceiver implements MessageListener {
  def xmlMessageReceiver
  void onMessage(Message message) {
    if (message instanceof TextMessage) {
      xmlMessageReceiver(message.getText())
    }
  }

  void onMessage(String xml) {
    xmlMessageReceiver(xml)
  }
}
