package org.powertac.broker

import grails.plugin.jms.Queue
import grails.converters.XML
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class JmsManagementService {

  static transactional = true
  static exposes = ["jms"]

  def jmsService
  def jmsConnectionFactory

  /**
   * The default queue name
   */
  @Queue(name = "brokers.defaultBroker.outputQueue")
  def receive(message) {
    log.info "Received from server: ${message}"
  }

  /**
   * Sends an object to the server. The objects automatically gets converted to a XML document.
   */
  def send(message) {
    def xml = message as XML
    log.info "Sending ${message} as ${xml}"
    jmsConnectionFactory.connectionFactory.brokerURL =  ConfigurationHolder.config.powertac.server
    jmsService.send("server.inputQueue", xml.toString())
  }
}
