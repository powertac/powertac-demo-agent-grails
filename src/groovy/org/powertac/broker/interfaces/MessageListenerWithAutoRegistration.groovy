package org.powertac.broker.interfaces

public interface MessageListenerWithAutoRegistration extends MessageListener {
  def getMessages()
}