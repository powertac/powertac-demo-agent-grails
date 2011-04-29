/*
 * Copyright (c) <current-year> by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.powertac.broker.infrastructure.messaging

import org.powertac.common.*
import org.joda.time.*

class MessageConverterIntegrationTests extends GroovyTestCase {
  MessageConverter messageConverter

  protected void setUp() {
    super.setUp()
    TariffSpecification.list()*.delete(flush: true)
    Broker.list()*.delete(flush: true)
    Rate.list()*.delete(flush: true)

  }

  protected void tearDown() {
    super.tearDown()
  }

  void testConvertTariffSpecification() {
    TariffSpecification tariffSpec = new TariffSpecification()
    def xml = messageConverter.toXML(tariffSpec)
    println "XML: \n${xml}"
    TariffSpecification convertedTS = messageConverter.fromXML(xml)

    assertEquals("tariffSpecification", tariffSpec.id, convertedTS.id);
  }

  void testConvertTariffSpecificationWithBroker() {
    Broker broker = new Broker(username: "bob").save(flush: true)
    assertNotNull(broker)
    TariffSpecification tariffSpec = new TariffSpecification(broker: broker)
    def xml = messageConverter.toXML(tariffSpec)
    println "XML: \n${xml}"
    TariffSpecification convertedTS = messageConverter.fromXML(xml)

    assertEquals("tariffSpecification", tariffSpec.id, convertedTS.id)
  }


  void testConvertTariffSpecificationSavedWithBroker() {
    Broker broker = new Broker(username: "bob").save(flush: true)
    assertNotNull(broker)
    Rate rate = new Rate()
    rate.setValue(0.121)
    rate.setDailyBegin(new DateTime(2011, 1, 1, 6, 0, 0, 0, DateTimeZone.UTC))
    rate.setDailyEnd(new DateTime(2011, 1, 1, 8, 0, 0, 0, DateTimeZone.UTC))

    if (!rate.save(flush: true)) {
      rate.errors.allErrors.each { println it }
    }
    assertNotNull(rate.save(flush: true))

    TariffSpecification tariffSpec = new TariffSpecification(broker: broker)
    tariffSpec.addToRates(rate)
    assertNotNull(tariffSpec.save(flush: true))
    def xml = messageConverter.toXML(tariffSpec)
    println "XML: \n${xml}"
    TariffSpecification convertedTS = messageConverter.fromXML(xml)

    assertEquals("tariffSpecification", tariffSpec.id, convertedTS.id)
  }
}
