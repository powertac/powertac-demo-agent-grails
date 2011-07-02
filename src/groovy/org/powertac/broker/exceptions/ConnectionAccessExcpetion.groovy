package org.powertac.broker.exceptions

public class ConnectionAccessExcpetion extends ServerLoginException {

  ConnectionAccessExcpetion () {
    super()
  }

  public ConnectionAccessExcpetion (String message) {
    super(message)

  }

  ConnectionAccessExcpetion (String s, Throwable throwable) {
    super(s, throwable)
  }

  ConnectionAccessExcpetion (Throwable throwable) {
    super(throwable)
  }
}
