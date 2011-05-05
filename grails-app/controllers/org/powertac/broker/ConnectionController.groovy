package org.powertac.broker

import grails.converters.XML
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.powertac.common.command.LoginRequestCmd
import org.powertac.common.command.LoginResponseCmd
import org.powertac.common.command.LoginResponseCmd.StatusCode
import org.powertac.common.Tariff.State
import org.powertac.broker.infrastructure.messaging.MessageReceiver

class ConnectionController {

  def competitionManagementService
  def jmsConnectionFactory

  def index = {
    if (jmsConnectionFactory.connectionFactory.brokerURL) {
      flash.message = "Already connected to ${jmsConnectionFactory.connectionFactory.brokerURL}, redirecting to status page"
      redirect controller: 'status'
      return
    }

    [server: "http://localhost:8080/powertac-server/",
        username: ConfigurationHolder.config.powertac.username,
        apiKey: ConfigurationHolder.config.powertac.apiKey]
  }

  def connect = {
    // Verify that server, username and api key are not null
    if (!params?.server || !params?.username) {
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
      def loginRequestCmd = new LoginRequestCmd(username: params.username, password: params.password) as XML

      // reestablish username and apiKey
      ConfigurationHolder.config.powertac.username = params.username
      ConfigurationHolder.config.powertac.apiKey = params.apiKey

      def loginRequest = loginRequestCmd.toString()
      def loginResponse = restClient.post(path: "api/login", body: loginRequest, requestContentType: ContentType.XML)

      if (loginResponse.status != 200) { // HTTP OK
        flash.message = "Server did not accept request. Status code: ${loginResponse.status}"
        redirect action: index
        return
      }

      //obsolte but maybe useful
      //.data returns GPathResult, which is very handy in general but we need the raw string here
      //def rawXMLString = XmlUtil.serialize(loginResponse.data);

      def loginResponseXml = loginResponse.data
      def loginResponseCmd = new LoginResponseCmd(status: loginResponseXml.status.text() as StatusCode,
          serverAddress: loginResponseXml.serverAddress, queueName: loginResponseXml.queueName)

      switch (loginResponseCmd.status) {
        case StatusCode.OK:
          competitionManagementService.initialize(loginResponseCmd)
          flash.message = "Sucessfully connected to ${loginResponseCmd.serverAddress}."
          redirect controller: 'status'
          break;
        case StatusCode.OK_BUSY:
          flash.message = "Login successful but server is busy right now. Please try again later."
          redirect action: index
          break;
        case StatusCode.ERR_USERNAME_NOT_FOUND:
          flash.message = "Username not found."
          redirect action: index
          break;
        case StatusCode.ERR_INVALID_APIKEY:
          flash.message = "Invalid API key."
          redirect action: index
          break;
        default:
          flash.message = "Invalid status code: ${loginResponseCmd.status}"
          redirect action: index
          break;
      }
    } catch (Exception e) {
      flash.message = "Could not connect to server: ${e.getMessage()}"
      log.error "${e.printStackTrace()}"
      redirect action: index
    }
  }
}
