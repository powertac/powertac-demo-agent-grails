/*
 * Copyright (c) 2011 by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.powertac.broker.core.tariffnegotiator

import org.apache.commons.logging.LogFactory
import org.powertac.broker.api.TariffNegotiator
import org.powertac.broker.infrastructure.messaging.MessageListenerRegistrar
import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.Rate
import org.powertac.common.TariffSpecification
import org.powertac.common.TariffTransaction

class DemoTariffNegotiator implements MessageListener, TariffNegotiator
{
  private static final log = LogFactory.getLog(this)

  MessageListenerRegistrar messageListenerRegistrar

  def getMessages () {
    [TariffSpecification, TariffTransaction]
  }

  def onMessage (TariffSpecification ts) {
    log.debug("onMessage(TariffSpecification) - start")

    def processedRates = []

    ts.rates.each { rate ->
      def dbRate = Rate.findById(rate.id)
      if (dbRate) {
        log.debug("onMessage(TariffSpecification) - found [dbRate.id:${dbRate.id},dbRate.version:${dbRate.version},rate.version:${rate.version}]")
        rate.version = dbRate.version
        rate = rate.merge()
        log.debug("onMessage(TariffSpecification) - after merge rate:${rate}")
      } else {
        log.debug("onMessage(TariffSpecification) - not found rate:${rate.id}")
        rate.save()
      }
      processedRates << rate
    }

    log.debug("onMessage(TariffSpecification) - there are ${ts.rates.size()} item in ts.rates")
    log.debug("onMessage(TariffSpecification) - there are ${processedRates.size()} item in processedRates")

    ts.rates.clear()
    ts.rates.addAll(processedRates)

    if (TariffSpecification.findById(ts.id)) {
      ts.merge()
    } else {
      ts.save()
    }

    log.debug("onMessage(TariffSpecification) - end")
  }

  def onMessage (TariffTransaction ttx) {
    log.debug("onMessage(TariffTransaction) - start")

    ttx.merge()

    log.debug("onMessage(TariffTransaction) - receving ${ttx.txType} ttx for ${ttx.broker.username}")
    log.debug("onMessage(TariffTransaction) - end")

  }

  def offerTariffs (tariffs) {
    // TODO: send tariffs to market
  }

  def setTariffOfferStatus (status) {
    // TODO: send final decision to market
  }
}
