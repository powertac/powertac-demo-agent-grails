package org.powertac.broker.exceptions

public class InvalidStatusCodeException extends ServerLoginException {
  public InvalidStatusCodeException (String message) {
    super(message)
  }

  InvalidStatusCodeException (String s, Throwable throwable) {
    super(s, throwable)
  }

  InvalidStatusCodeException (Throwable throwable) {
    super(throwable)
  }

  InvalidStatusCodeException () {
    super()
  }
}
