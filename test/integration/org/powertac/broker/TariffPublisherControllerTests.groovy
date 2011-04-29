package org.powertac.broker

import grails.test.ControllerUnitTestCase
import javax.jms.ObjectMessage
import javax.jms.Session
import org.powertac.common.TariffSpecification

class TariffPublisherControllerTests extends ControllerUnitTestCase {
  def tariffPublishingService
  def jmsConnectionFactory
  def tariffMessageConsumer
  def jmsService
  def conn

  def static final TARIFF_SPECIFICATION_BROADCAST_DESTINATION = 'TariffBroadcast'
  def static final BROKER_URL = 'tcp://localhost:61616'
  def static final TIMEOUT = 2000 // 2 seconds

  protected void setUp() {
    super.setUp()

    // set up brokerURL (not needed if already done in resources.groovy)
    jmsConnectionFactory.targetConnectionFactory.brokerURL = BROKER_URL

    conn = jmsConnectionFactory.createConnection()
    def session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)
    def tariffSpecBroadcastDest = session.createTopic(TARIFF_SPECIFICATION_BROADCAST_DESTINATION)

    tariffMessageConsumer = session.createConsumer(tariffSpecBroadcastDest)

    controller.tariffPublishingService = tariffPublishingService

    conn.start()
  }

  protected void tearDown() {

    conn.close()
    tariffMessageConsumer.close()

    super.tearDown()
  }

  void testPublish() {
    def tariffSpec = new TariffSpecification(brokerId: 1)

    mockParams.brokerId = tariffSpec.brokerId
    mockParams.powerType = tariffSpec.powerType
    mockParams.providerUrl = BROKER_URL
    mockParams.destinationName = TARIFF_SPECIFICATION_BROADCAST_DESTINATION

    controller.publish()

    def message = tariffMessageConsumer.receive(TIMEOUT)
    assertTrue(message instanceof ObjectMessage)

    def payload = message.getObject()
    assertTrue(payload instanceof TariffSpecification)

    // no equals implemented yet for full object equals
    assertEquals(tariffSpec.brokerId, payload.brokerId)
  }
}
