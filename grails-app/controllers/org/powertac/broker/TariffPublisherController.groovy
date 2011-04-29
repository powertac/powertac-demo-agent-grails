package org.powertac.broker

import org.powertac.common.Broker
import org.powertac.common.Rate
import org.powertac.common.TariffSpecification
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

    Rate rate = new Rate()
    rate.setValue(0.121)
    rate.setDailyBegin(new DateTime(2011, 1, 1, 6, 0, 0, 0, DateTimeZone.UTC))
    rate.setDailyEnd(new DateTime(2011, 1, 1, 8, 0, 0, 0, DateTimeZone.UTC))

    tariffSpec.addToRates(rate)

    if (!tariffSpec.validate()) {
      render(view: 'index', model: [tariffSpecificationInstance: tariffSpec])

    } else {

      tariffPublishingService.publish(tariffSpec)

      flash.message = 'Published tariffSpec'
      render(view: 'index')
    }
  }
}
