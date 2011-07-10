package org.powertac.broker

import org.powertac.common.CashPosition

class CashPositionService
{

  static transactional = true

  def getCashPosition (username) {
    def cp = CashPosition.withCriteria(uniqueResult: true) {
      broker {
        eq('username', username)
      }
    }

    log.debug("getCashPosition - XXXX broke ${cp?.broker?.username}:${username} has ${cp?.balance}")

    (cp?.balance != null) ?: 0.0
  }
}
