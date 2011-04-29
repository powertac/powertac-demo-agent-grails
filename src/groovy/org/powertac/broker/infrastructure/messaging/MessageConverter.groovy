/*
 * Copyright (c) <current-year> by the original author or authors.
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

package org.powertac.broker.infrastructure.messaging

import com.thoughtworks.xstream.XStream
import org.powertac.common.Competition
import org.powertac.common.msg.SimStart
import org.powertac.common.CustomerInfo
import org.powertac.common.CashPosition
import org.powertac.common.Timeslot
import org.powertac.common.ClearedTrade
import org.powertac.common.MarketPosition
import org.powertac.common.MarketTransaction
import org.powertac.common.Shout
import org.powertac.common.msg.TariffStatus
import org.powertac.common.TariffTransaction
import org.powertac.common.TariffSpecification
import org.powertac.common.Rate
import org.powertac.common.HourlyCharge
import org.powertac.common.msg.TariffUpdate
import org.powertac.common.msg.TariffExpire
import org.powertac.common.msg.TariffRevoke
import org.powertac.common.msg.VariableRateUpdate
import org.powertac.common.transformer.BrokerConverter


class MessageConverter implements org.springframework.beans.factory.InitializingBean
{

  XStream xstream

  void afterPropertiesSet ()
  {
    xstream = new XStream()
    xstream.processAnnotations(Competition.class)
    xstream.processAnnotations(SimStart.class)
    xstream.processAnnotations(CustomerInfo.class)
    xstream.processAnnotations(CashPosition.class)
    xstream.processAnnotations(Timeslot.class)
    xstream.processAnnotations(ClearedTrade.class)
    xstream.processAnnotations(MarketPosition.class)
    xstream.processAnnotations(MarketTransaction.class)
    xstream.processAnnotations(Shout.class)
    xstream.processAnnotations(TariffStatus.class)
    xstream.processAnnotations(TariffTransaction.class)
    xstream.processAnnotations(TariffSpecification.class)
    xstream.processAnnotations(Rate.class)
    xstream.processAnnotations(HourlyCharge.class)
    xstream.processAnnotations(TariffUpdate.class)
    xstream.processAnnotations(TariffExpire.class)
    xstream.processAnnotations(TariffRevoke.class)
    xstream.processAnnotations(VariableRateUpdate.class)
    xstream.registerConverter(new BrokerConverter())
  }

  String toXML(Object message) {
    xstream.toXML(message)
  }

  Object fromXML(String xml) {
    xstream.fromXML(xml)
  }
}
