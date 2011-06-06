package org.powertac.broker.infrastructure.messaging

import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.TextMessage

class MessageReceiver implements MessageListener
{
  def xmlMessageReceiver

  void onMessage (Message message) {
    if (message instanceof TextMessage) {
      onMessage(message.getText())
    }
  }

  void onMessage (String xml) {
    xmlMessageReceiver.onMessage(xml)
  }
}
