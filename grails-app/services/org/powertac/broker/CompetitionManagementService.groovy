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

package org.powertac.broker

import org.powertac.broker.infrastructure.messaging.MessageListenerRegistrar
import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.common.Timeslot
import org.powertac.common.msg.SimStart

class CompetitionManagementService implements MessageListener
{
  static transactional = true

  def jmsConnectionFactory
  def jmsManagementService
  def messageReceiver

  def initialize (loginResponseCmd)
  {
    // Generate broker URL and set it. Connection will be established automatically.
    jmsConnectionFactory.connectionFactory.brokerURL = loginResponseCmd.serverAddress
    jmsManagementService.registerBrokerMessageListener(loginResponseCmd.queueName, messageReceiver)

    jmsManagementService.register(Competition, this)
    jmsManagementService.register(SimStart, this)
    jmsManagementService.register(Timeslot, this)

  }


  def onMessage (Competition competition)
  {
    log.debug("Saving competition ${competition.id}")
    competition.save()
  }

  def onMessage (SimStart simStart)
  {
    log.debug("onMessage(SimStart) - start")
    log.debug("Saving simStart - start @ ${simStart.start}")

    simStart.brokers?.each {
      log.debug("Populate broker: ${it}")
      def broker = new Broker(username: it, enabled: true)
      broker.save()
    }


    log.debug("onMessage(SimStart) - end")
  }


  def onMessage (Timeslot slot)
  {
    log.debug("onMessage(Timeslot) - start")

    slot.save()
    log.debug("slot #${slot.serialNumber}, start@${slot.startInstant}, end@${slot.endInstant}")


    log.debug("onMessage(Timeslot) - end")
  }
}