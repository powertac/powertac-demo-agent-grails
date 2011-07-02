package org.powertac.broker

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AutoLoginService
{
  static transactional = false

  def connectionService

  boolean autoLogin = false
  boolean initialized = false
  boolean connected = false

  def initialize () {
    def configLoginMode = ConfigurationHolder.config?.powertac?.broker?.login?.mode
    if (configLoginMode == 'auto') {
      autoLogin = true
      log.debug("initialize - config autoLogin:true")
    }

    def cliLoginMode = System.properties.getProperty('broker.login.mode')
    if (cliLoginMode == 'auto') {
      autoLogin = true
      log.debug("initialize - cli autoLogin:true")
    } else {
      log.debug("initialize - cli:${cliLoginMode}")
    }

    initialized = true
  }

  def login () {
    if (initialized) {
      if (autoLogin && !connected) {
        def params = [server: ConfigurationHolder.config.powertac.server,
            username: ConfigurationHolder.config.powertac.username,
            apiKey: ConfigurationHolder.config.powertac.apiKey]
        try {
          connectionService.connect(params)
          connected = connectionService.isConnected()
        } catch (e) {
          log.info("Failed to connect to server: ${e.message}")
        }
      }
    } else {
      initialize()
    }
  }
}
