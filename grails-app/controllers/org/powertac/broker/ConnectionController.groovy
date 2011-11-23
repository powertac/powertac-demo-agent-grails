package org.powertac.broker

import grails.converters.XML
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.powertac.common.Tariff.State
import org.powertac.broker.infrastructure.messaging.MessageReceiver
import org.powertac.broker.exceptions.InvalidParameterException

class ConnectionController {

  def competitionManagementService
  def connectionService
  def jmsConnectionFactory

  def index = {
    if (competitionManagementService.isConnected()) {
      flash.message = "Already connected to ${competitionManagementService.getBrokerUrl()}, redirecting to status page"
      redirect controller: 'status'
      return
    }

    [server: "http://localhost:8080/powertac-server/",
        username: ConfigurationHolder.config.powertac.username,
        apiKey: ConfigurationHolder.config.powertac.apiKey]
  }

  def connect = {
    log.debug("connect - start with ${params}")
    try {
      connectionService.connect(params)
      flash.message = "Sucessfully connected to server."
      redirect controller: 'status'
    } catch (e) {
      log.error("Encounter exception - ${e.message}", e)
      flash.message = e.message
      redirect action: index
    }
    log.debug("connect - end")
  }

  def getConnectionStatusText = {
    if (competitionManagementService.isConnected()) {
      render "connected to ${competitionManagementService.brokerUrl}"
    } else {
      render "not connected"
    }
  }
}
