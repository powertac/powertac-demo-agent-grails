package org.powertac.broker

import org.powertac.common.Broker
import org.powertac.common.Rate
import org.powertac.common.TariffSpecification
import org.powertac.common.enumerations.PowerType
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class TariffPublisherController {
  def tariffPublishingService

  def index = {
    render(view: 'index')
  }

  def publish = {
    def tariffSpec = new TariffSpecification()
    bindData(tariffSpec, params, [include: ['minDuration', 'earlyWithdrawPayment', 'powerType', 'periodicPayment', 'signupPayment']])

    log.debug("Username: ${params.username}")
    tariffSpec.broker = new Broker(username: params.broker, password: "")

    if (tariffSpec.powerType == PowerType.CONSUMPTION) { 
      Rate rate = new Rate()
      rate.setValue(0.08)
      rate.setDailyBegin(23)
      rate.setDailyEnd(6)
      rate.save()
      tariffSpec.addToRates(rate)
    
      rate = new Rate()
      rate.setValue(0.14)
      rate.setDailyBegin(7)
      rate.setDailyEnd(22)
      rate.save()
      tariffSpec.addToRates(rate)
    }
    else if (tariffSpec.powerType == PowerType.PRODUCTION) {
      Rate rate = new Rate()
      rate.setValue(-0.05)
      rate.save()
      tariffSpec.addToRates(rate)
    }

    if (!tariffSpec.validate()) {
      tariffSpec.save()
      render(view: 'index', model: [tariffSpecificationInstance: tariffSpec])

    } else {

      tariffPublishingService.publish(tariffSpec)

      flash.message = 'Published tariffSpec'
      render(view: 'index')
    }
  }
}
