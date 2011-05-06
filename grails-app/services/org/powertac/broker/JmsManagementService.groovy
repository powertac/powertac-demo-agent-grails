package org.powertac.broker

import javax.jms.MessageListener
import org.springframework.jms.listener.DefaultMessageListenerContainer
import grails.converters.XML
import org.powertac.common.MessageConverter

class JmsManagementService {

  static transactional = true
  def jmsConnectionFactory
  def jmsService
  def transactionManager

  MessageConverter messageConverter

  def registerBrokerMessageListener(String destinationName, MessageListener listener) {
    log.info("creating listener container for ${destinationName}")
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer()
    container.setConnectionFactory(jmsConnectionFactory)
    container.setDestinationName(destinationName)
    container.setMessageListener(listener)
    container.setTransactionManager(transactionManager)
    container.afterPropertiesSet()
    container.start()
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
