package org.powertac.broker

import org.powertac.common.Broker
import org.powertac.common.Rate
import org.powertac.common.TariffSpecification
import org.powertac.common.enumerations.PowerType
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class TariffPublisherController {
  def tariffPublishingService

  def index = {
    render(view: 'index')
  }

	def create = {
		render(view: 'index')
	}

  def publish = {
	  def username = ConfigurationHolder.config.powertac.username
    TariffSpecification tariffSpec = new TariffSpecification(new Broker(username), PowerType.CONSUMPTION)
    bindData(tariffSpec, params, [include: ['minDuration', 'earlyWithdrawPayment', 'powerType', 'periodicPayment', 'signupPayment']])

    if (tariffSpec.powerType == PowerType.CONSUMPTION) { 
      Rate rate = new Rate().withValue(0.08).withDailyBegin(23).withDailyEnd(6)
      tariffSpec.addRate(rate)
    
      rate = new Rate().withValue(0.14).withDailyBegin(7).withDailyEnd(22)
      tariffSpec.addRate(rate)
    }
    else if (tariffSpec.powerType == PowerType.PRODUCTION) {
      Rate rate = new Rate().withValue(-0.05)
      tariffSpec.addToRate(rate)
    }

		tariffPublishingService.publish(tariffSpec)
		flash.message = 'Published tariffSpec'
		render(view: 'index')
  }
}
