package org.powertac.broker

class LoginJob
{
  def concurrent = false

  static triggers = { simple name: 'autologin', repeatInterval: 1000 }

  def autoLoginService

  def execute () {
    log.debug("execute - start")
    if (!autoLoginService.isInitialized()) {
      autoLoginService.initialize()
    }

    if (autoLoginService.isAutoLogin()) {
      log.debug("execute - autoLogin:true")
      autoLoginService.login()
    } else {
      log.debug("execute - autoLogin:false")
    }

    log.debug("execute - end")
  }
}
