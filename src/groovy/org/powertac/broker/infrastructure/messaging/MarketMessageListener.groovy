package org.powertac.broker.infrastructure.messaging

import org.apache.commons.logging.LogFactory

import org.powertac.broker.interfaces.MessageListenerWithAutoRegistration
import org.powertac.common.command.*
import org.powertac.broker.infrastructure.persistence.GormBasedMessagePersistenceManager

class MarketMessageListener implements MessageListenerWithAutoRegistration
{
  private static final log = LogFactory.getLog(this)

  def messagePersistenceManager

  // TODO: retrieve all classes within cmd package programmatically
  static final def transientClazz = [SimEnd, ArrayList, CustomerBootstrapData,
      CustomerList, ErrorCmd, SimStart, SimPause, SimResume]

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
}
