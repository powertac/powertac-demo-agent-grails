package org.powertac.broker.exceptions

public class ServerBusyException extends ServerLoginException {

  ServerBusyException () {
    super()
  }

  public ServerBusyException (String message) {
    super(message)
  }

  ServerBusyException (String s, Throwable throwable) {
    super(s, throwable)
  }

  ServerBusyException (Throwable throwable) {
    super(throwable)
  }
}
