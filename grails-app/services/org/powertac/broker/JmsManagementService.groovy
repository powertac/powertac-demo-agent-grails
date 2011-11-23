package org.powertac.broker

import org.springframework.jms.listener.DefaultMessageListenerContainer

import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.XMLMessageConverter

class JmsManagementService {

  static transactional = false
  def jmsConnectionFactory
  def jmsService
  def transactionManager
  def messageListenerRegistrar
	def serverQueueName = "server.inputQueue"

  def listenerContainerMap = [:]

  XMLMessageConverter messageConverter

  def registerBrokerMessageListener(String destinationName, javax.jms.MessageListener listener) {
    log.debug("registerBrokerMessageListener - start [creating listener container for ${destinationName}]")
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer()
    container.setConnectionFactory(jmsConnectionFactory)
    container.setDestinationName(destinationName)
    container.setMessageListener(listener)
    container.setTransactionManager(transactionManager)
    container.setSessionTransacted(true)
    container.afterPropertiesSet()
    container.start()

    listenerContainerMap.put(listener, container)

    log.debug("registerBrokerMessageListener - end")
  }

  def unregisterBrokerMessageListener(listener) {
    DefaultMessageListenerContainer container =  listenerContainerMap.get(listener)
    if (container) {
      if (container.isActive()) {
        log.debug("unregisterBrokerMessageListener - shutting down container for ${listener.class.simpleName}")
        container?.shutdown()
      } else {
        log.info("unregisterBrokerMessageListener - container for ${listener.class.simpleName} is not active")
      }

      listenerContainerMap.remove(listener)
    } else {
      log.info("unregisterBrokerMessageListener - could not find container for ${listener.class.simpleName}")
    }
  }

  def register(Class messageType, MessageListener listener) {
    messageListenerRegistrar.register(messageType, listener)
  }

  def unregister(Class messageType, MessageListener listener) {
    messageListenerRegistrar.unregister(messageType, listener)
  }

	def initialize(serverQueueName) {
		this.serverQueueName = serverQueueName
	}

  /**
   * Sends an object to the server. The objects automatically gets converted to a XML document.
   */
  def send(message) {
    log.debug("sending message ${message.class.name}")

    def xml = messageConverter.toXML(message)
    log.debug("convert to xml: \n${xml}")

	  log.debug("sending JMS message to ${serverQueueName}")
    jmsService.send(serverQueueName, messageConverter.toXML(message))
  }
}
