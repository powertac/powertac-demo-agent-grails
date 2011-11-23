package org.powertac.broker.infrastructure.messaging

import org.apache.commons.logging.LogFactory

import org.powertac.broker.interfaces.MessageListenerWithAutoRegistration
import org.powertac.common.msg.CustomerBootstrapData
import org.powertac.common.msg.SimEnd
import org.powertac.common.msg.SimStart
import org.powertac.common.msg.SimPause
import org.powertac.common.msg.SimResume
import org.powertac.common.msg.BrokerAccept
import org.powertac.common.IdGenerator

class MarketMessageListener implements MessageListenerWithAutoRegistration
{
  private static final log = LogFactory.getLog(this)

  def messagePersistenceManager

  // TODO: retrieve all classes within cmd package programmatically
  static final def transientClazz = [SimEnd, ArrayList, CustomerBootstrapData,
     SimStart, SimPause, SimResume, BrokerAccept]

  def getMessages () {
    [Object]
  }

  def onMessage (Object msg) {
    log.debug("onMessage(${msg.class.name}) - start")
    if (!transientClazz.contains(msg.class)) {
      log.debug("onMessage(${msg.class.name}) - saving...")
      messagePersistenceManager.save(msg)
    }
    log.debug("onMessage(${msg.class.name}) - end")
  }

  def onMessage (CustomerBootstrapData cbd) {
    log.debug("onMessage(CustomerBootstrapData) - start")
    messagePersistenceManager.save(cbd.customer)
    log.debug("onMessage(CustomerBootstrapData) - end")
  }

	def onMessage (BrokerAccept brokerAccept) {
		log.debug("onMessage(BrokerAccept) - start")
		IdGenerator.setPrefix(brokerAccept.prefix)
		log.debug("onMessage(BrokerAccept) - end")
	}
}
