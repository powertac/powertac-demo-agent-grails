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

    message.rates.each { log.debug("trying to convert rate: \n${messageConverter.toXML(it)}") }
    def grailsConverterXML = message as XML
    log.debug("trying to convert with grails:\n${grailsConverterXML}")

    log.debug("value of broker is ${message.broker}")
    def brokerXML = "null"
    if (message.broker != null) {
      log.debug("how did i get in here???? ${message.broker}:${message.broker != null}")
      brokerXML = messageConverter.toXML(message.broker)
    } else {
      log.debug("yessss, broker is null")
    }

    log.debug("trying to convert brokers:\n${brokerXML}")

    def xml = messageConverter.toXML(message)

    log.debug("convert to xml: \n${xml}")
    jmsService.send("server.inputQueue", messageConverter.toXML(message))
  }
}
