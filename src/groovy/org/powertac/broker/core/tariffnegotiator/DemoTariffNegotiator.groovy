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
import org.powertac.common.msg.TariffStatus

class DemoTariffNegotiator implements MessageListener, TariffNegotiator
{
  private static final log = LogFactory.getLog(this)

  def getMessages () {
    [TariffSpecification, TariffTransaction, TariffStatus]
  }

  def onMessage (TariffSpecification ts) {
    log.debug("onMessage(TariffSpecification) - start")
    log.debug("onMessage(TariffSpecification) - end")
  }

  def onMessage (TariffTransaction ttx) {
    log.debug("onMessage(TariffTransaction) - start")
    log.debug("onMessage(TariffTransaction) - end")

  }

  def onMessage (TariffStatus ts) {
    log.debug("onMessage(TariffStatus) - start")
    log.debug("onMessage(TariffStatus) - start")
  }

  def offerTariffs (tariffs) {
    // TODO: send tariffs to market
  }

  def setTariffOfferStatus (status) {
    // TODO: send final decision to market
  }
}
