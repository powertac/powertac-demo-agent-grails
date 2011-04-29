package org.powertac.broker

import grails.plugin.jms.Queue
import org.powertac.common.TariffSpecification;

class TariffPublishingService {

  static transactional = true

  def jmsManagementService

  def publish(TariffSpecification ts) {
    jmsManagementService.send(ts)
  }
}
