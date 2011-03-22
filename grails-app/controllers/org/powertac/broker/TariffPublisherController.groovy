package org.powertac.broker

import org.powertac.common.TariffSpecification;

class TariffPublisherController {
	def tariffPublishingService

    def index = {
		render(view:'index')
	}
	
	def publish = {
		def tariffSpec = new TariffSpecification(params)
		def providerUrl = params.providerUrl
		def destinationName = params.destinationName

		tariffPublishingService.publish(tariffSpec, providerUrl, destinationName)
				
		render(tariffSpec.toString() 
			   + "<br>providerUrl:" + providerUrl
			   + "<br>destinationName:" + destinationName)
	}
}
