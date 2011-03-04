package org.powertac.broker

import org.powertac.common.TariffSpecification;

class TariffPublishingService {
	def jmsConnectionFactory
	def jmsService
	
    static transactional = false

    def publish(TariffSpecification ts, String brokerURL, String destinationName) {
		if (log.isDebugEnabled()) {
			log.debug("start publish")
		}
		jmsConnectionFactory.targetConnectionFactory.brokerURL = brokerURL
		jmsService.send(topic: destinationName, ts)
		if (log.isDebugEnabled()) {
			log.debug("end publish")
		}			    
	}
}
