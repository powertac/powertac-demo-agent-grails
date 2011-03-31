package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient
import org.powertac.common.command.LoginRequestCmd
import grails.converters.XML
import groovyx.net.http.ContentType

class ConnectionController {

  def jmsConnectionFactory

  def index = {
    [server: "http://localhost:8080/powertac-server/",
        username: ConfigurationHolder.config.powertac.username,
        apiKey: ConfigurationHolder.config.powertac.apiKey]
  }

  def connect = {
    // Verify that server, username and api key are not null
    if (!params?.server || !params?.username || !params.apiKey) {
      flash.message = "No server, username and/or apiKey specified!"
      redirect action: index
      return
    }

    // Create REST client
    def restClient
    try {
      restClient = new RESTClient(params.server)
    } catch (Exception e) {
      flash.message = "Invalid Server URL: ${e.getMessage()}"
      redirect action: index
      return
    }

    // Connect to server
    try {
      def loginRequestCmd = new LoginRequestCmd(username: params.username, apiKey: params.apiKey) as XML
      def loginRequest = loginRequestCmd.toString()
      def loginResponse = restClient.post(path:"api/login", body:loginRequest, requestContentType: ContentType.XML)
//      log.error "status ${loginResponse.status}"
//      log.error "data ${loginResponse.data}"    // XML
    } catch (Exception e) {
      flash.message = "Could not connect to server: ${e.getMessage()}"
      redirect action: index
      return
    }

//    jmsConnectionFactory.connectionFactory.brokerURL =  params?.server

    flash.message = "Connected."
    redirect action: index
  }
}
