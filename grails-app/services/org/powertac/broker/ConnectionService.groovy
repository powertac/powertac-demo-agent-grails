package org.powertac.broker

import groovyx.net.http.RESTClient
import grails.converters.XML
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.ContentType
import org.powertac.broker.exceptions.InvalidParameterException
import org.powertac.broker.exceptions.InvalidConnectionException
import org.powertac.broker.exceptions.ConnectionAccessExcpetion
import org.powertac.broker.exceptions.ServerBusyException

import org.powertac.broker.exceptions.ServerAuthenticationException
import org.powertac.broker.exceptions.InvalidStatusCodeException

import org.powertac.broker.exceptions.ConnectionAccessException
import org.powertac.common.msg.LoginRequestCmd
import org.powertac.common.msg.LoginResponseCmd
import org.powertac.common.msg.LoginResponseCmd.StatusCode
import org.powertac.common.repo.BrokerRepo
import org.powertac.common.Broker

class ConnectionService
{
  static transactional = false

  def competitionManagementService
	BrokerRepo brokerRepo

  def connect (params)
  {

    // Verify that server, username and api key are not null
    if (!params?.server || !params?.username) {
      throw new InvalidParameterException("No server and/or username specified!")
    }

    if (params.bypassAuthentication) {
      log.error("bypassing authentication")
			Broker broker = new Broker(params.username)

			brokerRepo.add(broker)

			// reestablish username
			ConfigurationHolder.config.powertac.username = params.username

	    // default names for standalone configuration
      LoginResponseCmd loginResponseCmd = new LoginResponseCmd(StatusCode.OK, params.server, 'serverInput', new Broker(params.username).toQueueName())
      competitionManagementService.initialize(loginResponseCmd)
    } else {
      // Create REST client
			def restClient
			try {
				restClient = new RESTClient(params.server)
			} catch (Exception e) {
				throw new InvalidConnectionException("Invalid Server URL: ${e.getMessage()}ß", e)
			}

			// Connect to server
			try {
				def loginRequestCmd = new LoginRequestCmd(params.username, params.password) as XML

				// reestablish username
				ConfigurationHolder.config.powertac.username = params.username

				def loginRequest = loginRequestCmd.toString()
				def loginResponse = restClient.post(path: "api/login", body: loginRequest, requestContentType: ContentType.XML)

				if (loginResponse.status != 200) { // HTTP OK
					throw new ConnectionAccessExcpetion("Server did not accept request. Status code: ${loginResponse.status}")
				}

				def loginResponseXml = loginResponse.data
				def loginResponseCmd = new LoginResponseCmd(status: loginResponseXml.status.text() as StatusCode,
						serverAddress: loginResponseXml.serverAddress, queueName: loginResponseXml.queueName)

				switch (loginResponseCmd.status) {
					case StatusCode.OK:
						competitionManagementService.initialize(loginResponseCmd)
						break;
					case StatusCode.OK_BUSY:
						throw new ServerBusyException("Server is busy right now. Please try again later.")
						break;
					case StatusCode.ERR_USERNAME_NOT_FOUND:
						throw new ServerAuthenticationException("Username not found.")
						break;
					default:
						throw new InvalidStatusCodeException("Invalid status code: ${loginResponseCmd.status}")
						break;
				}
			} catch (Exception e) {
				throw new ConnectionAccessException("Could not connect to server: ${e.getMessage()}", e)
			}
    }
  }

  def disconnect() {

  }

  def isConnected() {
    competitionManagementService.isConnected()
  }
}

