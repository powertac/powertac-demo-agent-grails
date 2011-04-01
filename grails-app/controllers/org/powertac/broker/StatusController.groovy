package org.powertac.broker

import grails.util.Metadata

class StatusController {

  def index = {
    def metaData = Metadata.current
    [brokerProperties: ["App version": metaData[Metadata.APPLICATION_VERSION],
        "Grails version": metaData[Metadata.APPLICATION_GRAILS_VERSION],
        "Groovy version" : org.codehaus.groovy.runtime.InvokerHelper.getVersion(),
        "JVM version" : System.getProperty('java.version'),
        "Controllers" : grailsApplication.controllerClasses.size(),
        "Domains" : grailsApplication.domainClasses.size(),
        "Services" : grailsApplication.serviceClasses.size(),
        "Tag Libraries" : grailsApplication.tagLibClasses.size()
    ]]
  }
}