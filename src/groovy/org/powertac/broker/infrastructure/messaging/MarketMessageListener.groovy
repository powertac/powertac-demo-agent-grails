package org.powertac.broker.infrastructure.messaging

import org.apache.commons.logging.LogFactory
import org.powertac.broker.infrastructure.persistence.MessagePersistenceManager
import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.command.SimEnd;
import org.springframework.transaction.annotation.Transactional
import org.powertac.broker.interfaces.MessageListenerWithAutoRegistration
import org.powertac.common.command.CustomerBootstrapData
import org.powertac.common.command.SimStart
import org.powertac.common.command.ErrorCmd
import org.powertac.common.command.CustomerList
import org.powertac.common.command.SimResume
import org.powertac.common.command.SimPause;

class MarketMessageListener implements MessageListenerWithAutoRegistration
{
  private static final log = LogFactory.getLog(this)

  MessagePersistenceManager messagePersistenceManager
  // TODO: retrieve all classes within cmd package programmatically
  static final def transientClazz = [SimEnd, ArrayList, CustomerBootstrapData,
      CustomerList, ErrorCmd, SimStart, SimPause, SimResume]

  def getMessages () {
    [Object]
  }

  @Transactional
  def onMessage (Object msg) {
    log.debug("onMessage(${msg.class.name}) - start")
    if (!transientClazz.contains(msg.class)) {
      log.debug("onMessage(${msg.class.name}) - saving...")
      messagePersistenceManager.save(msg)
    }
    log.debug("onMessage(${msg.class.name}) - end")
  }
}