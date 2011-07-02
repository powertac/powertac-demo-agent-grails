package org.powertac.broker.exceptions

public class ConnectionAccessException extends ServerLoginException{

  ConnectionAccessException () {
    super()
  }

  ConnectionAccessException (String s) {
    super(s)
  }

  public ConnectionAccessException (String message, Throwable e) {

    super(message, e)
  }

  ConnectionAccessException (Throwable throwable) {
    super(throwable)
  }
}
