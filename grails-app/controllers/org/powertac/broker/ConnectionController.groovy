package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class ConnectionController {

  def jmsConnectionFactory

  def index = {
    [server: "http://localhost:8080/powertac-server/",
        username: ConfigurationHolder.config.powertac.username,
        apiKey: ConfigurationHolder.config.powertac.apiKey]
  }

  def connect = {
    if (!params?.server) {
      flash.message = "No server specified."
    }



//    jmsConnectionFactory.connectionFactory.brokerURL =  params?.server

    flash.message = "Connected."
    redirect action: index
  }
}
