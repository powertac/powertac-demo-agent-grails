package org.powertac.broker

import grails.plugin.jms.Queue
import grails.converters.XML
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.jms.listener.DefaultMessageListenerContainer
import javax.jms.MessageListener


class JmsManagementService {

  static transactional = true
  static exposes = ["jms"]

  def jmsConnectionFactory
  def jmsService

  def registerBrokerMessageListener(String username, MessageListener listener) {
    log.info("creating listener container for ${username}")
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer()
    container.setConnectionFactory(jmsConnectionFactory)
    container.setDestinationName("brokers.${username}.outputQueue")
    container.setMessageListener(listener)
    container.afterPropertiesSet()
    container.start()
  }

  /**
   * Sends an object to the server. The objects automatically gets converted to a XML document.
   */
  def send(message) {
    def xml = message as XML
    log.info "Sending ${message} as ${xml}"
    jmsService.send("server.inputQueue", xml.toString())
  }
}
