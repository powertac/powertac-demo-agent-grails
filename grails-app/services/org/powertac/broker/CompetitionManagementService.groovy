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

import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.Broker
import org.powertac.common.Competition
import org.powertac.common.msg.SimStart
import org.powertac.common.msg.TimeslotUpdate

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
    jmsManagementService.register(TimeslotUpdate, this)

  }


  def onMessage (Competition competition)
  {
    log.debug("onMessage(Competition) - start")

    competition.brokers?.each {
      log.debug("onMessage(Competition) - populate broker: ${it}")
      def broker = new Broker(username: it, enabled: true)
      broker.save()
    }

    log.debug("onMessage(Competition) - saving competition ${competition}:${competition.save() ? 'successful' : competition.errors}")
    log.debug("onMessage(Competition) - end")
  }

  def onMessage (SimStart simStart)
  {
    log.debug("onMessage(SimStart) - start")
    log.debug("Saving simStart - start @ ${simStart.start}")

    simStart.save()

    log.debug("onMessage(SimStart) - end")
  }


  def onMessage (TimeslotUpdate slotUpdate)
  {
    log.debug("onMessage(TimeslotUpdate) - start")

    log.debug("onMessage(TimeslotUpdate) - received TimeslotUpdate: ${slotUpdate.id}")

    def newEnableds = []
    slotUpdate.enabled?.each {
      it.id = it.serialNumber
      it.enabled = true
      it.endInstant = it.startInstant // FIXME
      log.debug("onMessage(TimeslotUpdate) -    saving enabled timeslot ${it.id}: ${(newEnableds << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.enabled = newEnableds

    def newDisables = []
    slotUpdate.disabled?.each {
      it.id = it.serialNumber
      it.enabled = false
      it.endInstant = it.startInstant // FIXME
      log.debug("onMessage(TimeslotUpdate) -    saving disabled timeslot ${it.id}: ${(newDisables << it.merge()) ? 'successful' : it.errors}")
    }
    slotUpdate.disabled = newDisables

    log.debug("onMessage(TimeslotUpdate) - saving TimeslotUpdate ${slotUpdate.id}:${slotUpdate.save() ? 'successful' : slotUpdate.errors}")

    log.debug("onMessage(TimeslotUpdate) - end")
  }
}