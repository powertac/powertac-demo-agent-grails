package org.powertac.broker

import org.powertac.common.CashPosition
import org.powertac.common.Broker

class CashPositionService
{

  static transactional = true

  def getCashPosition (username) {
    Broker broker = Broker.findByUsername(username)
    def cp = broker?.cash
    log.debug("getCashPosition - XXXX broke ${cp?.broker?.username}:${username} has ${cp?.balance}")

    (cp?.balance) ?: 0.0
  }
}
