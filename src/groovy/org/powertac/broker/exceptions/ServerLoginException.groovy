package org.powertac.broker.exceptions

public class ServerLoginException extends RuntimeException {

  ServerLoginException () {
    super()
  }

  ServerLoginException (String s) {
    super(s)
  }

  ServerLoginException (String s, Throwable throwable) {
    super(s, throwable)
  }

  ServerLoginException (Throwable throwable) {
    super(throwable)
  }
}
