package org.powertac.broker

import org.powertac.common.CashPosition
import org.powertac.common.Broker
import org.powertac.common.repo.BrokerRepo

class CashPositionService
{

  static transactional = true
  BrokerRepo brokerRepo

  def getCashPosition (username) {
    Broker broker = brokerRepo.findByUsername(username)
    def cp = broker?.cash
    log.debug("getCashPosition - XXXX broke ${cp?.broker?.username}:${username} has ${cp?.balance}")

    (cp?.balance) ?: 0.0
  }
}
