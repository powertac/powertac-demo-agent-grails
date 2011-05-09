package org.powertac.broker

import org.springframework.jms.listener.DefaultMessageListenerContainer
import grails.converters.XML
import org.powertac.common.MessageConverter
import org.powertac.broker.interfaces.MessageListener

class JmsManagementService {

  static transactional = true
  def jmsConnectionFactory
  def jmsService
  def transactionManager
  def messageListenerRegistrar

  MessageConverter messageConverter

  def registerBrokerMessageListener(String destinationName, javax.jms.MessageListener listener) {
    log.info("creating listener container for ${destinationName}")
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer()
    container.setConnectionFactory(jmsConnectionFactory)
    container.setDestinationName(destinationName)
    container.setMessageListener(listener)
    container.setTransactionManager(transactionManager)
    container.afterPropertiesSet()
    container.start()
  }

  def register(Class messageType, MessageListener listener) {
    messageListenerRegistrar.register(messageType, listener)
  }

  /**
   * Sends an object to the server. The objects automatically gets converted to a XML document.
   */
  def send(message) {
    log.debug("sending message ${message.class.name}")

    def xml = messageConverter.toXML(message)
    log.debug("convert to xml: \n${xml}")

    jmsService.send("server.inputQueue", messageConverter.toXML(message))
  }
}
