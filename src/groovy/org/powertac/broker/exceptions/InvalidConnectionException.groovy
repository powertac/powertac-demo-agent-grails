package org.powertac.broker.exceptions

public class InvalidConnectionException extends ServerLoginException {

  InvalidConnectionException () {
    super()
  }

  InvalidConnectionException (String s) {
    super(s)
  }

  public InvalidConnectionException (String message, Throwable o) {
    super(message, o)

  }

  InvalidConnectionException (Throwable throwable) {
    super(throwable)
  }
}
