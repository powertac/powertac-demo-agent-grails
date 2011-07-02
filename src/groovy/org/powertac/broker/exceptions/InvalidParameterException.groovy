package org.powertac.broker.exceptions

public class InvalidParameterException extends ServerLoginException {

  InvalidParameterException () {
    super()
  }

  InvalidParameterException (String s) {
    super(s)
  }

  InvalidParameterException (String s, Throwable throwable) {
    super(s, throwable)
  }

  InvalidParameterException (Throwable throwable) {
    super(throwable)
  }
}
