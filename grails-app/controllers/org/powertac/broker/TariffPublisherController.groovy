package org.powertac.broker

import org.powertac.common.TariffSpecification;

class TariffPublisherController {
  def tariffPublishingService

  def index = {
    render(view:'index')
  }      

  def publish = {
    def tariffSpec = new TariffSpecification(params)
    log.info "IN: tariffSpec.expiration: ${params.expiration}"
    log.info "OUT: tariffSpec.expiration: ${tariffSpec.expiration}"
    def providerUrl = params.providerUrl
    def destinationName = params.destinationName

    tariffPublishingService.publish(tariffSpec, providerUrl, destinationName)
    flash.message='Published tariffSpec'

    render(view:'index')
    }
}
