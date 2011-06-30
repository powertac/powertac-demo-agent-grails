package org.powertac.broker.infrastructure.messaging

import org.apache.commons.logging.LogFactory
import org.powertac.broker.infrastructure.persistence.MessagePersistenceManager
import org.powertac.broker.interfaces.MessageListener
import org.powertac.common.command.SimEnd;
import org.powertac.common.command.SimStart;

class MarketMessageListener implements MessageListener
{
  private static final log = LogFactory.getLog(this)

  MessagePersistenceManager messagePersistenceManager
  static final def transientClazz = [SimEnd, ArrayList]

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
}