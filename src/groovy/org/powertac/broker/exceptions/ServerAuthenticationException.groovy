package org.powertac.broker.exceptions

public class ServerAuthenticationException extends ServerLoginException {
  public ServerAuthenticationException (String message) {
    super(message)
  }

  ServerAuthenticationException (String s, Throwable throwable) {
    super(s, throwable)
  }

  ServerAuthenticationException (Throwable throwable) {
    super(throwable)
  }

  ServerAuthenticationException () {
    super()
  }
}
