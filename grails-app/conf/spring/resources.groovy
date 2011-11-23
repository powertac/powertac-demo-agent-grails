
beans = {
//  jmsConnectionFactory(org.springframework.jms.connection.SingleConnectionFactory) {bean ->
//    bean.destroyMethod = "stop"
//    connectionFactory = {org.apache.activemq.ActiveMQConnectionFactory cf ->
//      brokerURL = ''
//    }
//  }
  jmsConnectionFactory(org.springframework.jms.connection.SingleConnectionFactory) {bean ->
    targetConnectionFactory = {org.apache.activemq.ActiveMQConnectionFactory cf ->
      brokerURL = ''
    }
  }

  messageListenerRegistrar(org.powertac.broker.infrastructure.messaging.MessageListenerRegistrar)

  xmlMessageReceiver(org.powertac.broker.infrastructure.messaging.XMLMessageReceiver) {
    messageListenerRegistrar = messageListenerRegistrar
    messageConverter = ref('messageConverter')
  }

  messageReceiver(org.powertac.broker.infrastructure.messaging.MessageReceiver) {
    xmlMessageReceiver = xmlMessageReceiver
  }

  arrayListProcessor(org.powertac.broker.infrastructure.messaging.ArrayListProcessor) { bean ->
    messageListenerRegistrar = messageListenerRegistrar
  }

  messagePersistenceManager(org.powertac.broker.infrastructure.persistence.GormBasedMessagePersistenceManager)

  marketMessageListener(org.powertac.broker.infrastructure.messaging.MarketMessageListener)  { bean ->
    messagePersistenceManager = messagePersistenceManager
  }

  tariffNegotiator(org.powertac.broker.core.tariffnegotiator.DemoTariffNegotiator)
  springApplicationContext(org.powertac.common.spring.SpringApplicationContext)
  messageConverter(org.powertac.common.XMLMessageConverter)
  brokerRepo(org.powertac.common.repo.BrokerRepo)
  customerRepo(org.powertac.common.repo.CustomerRepo)
	timeslotRepo(org.powertac.common.repo.TimeslotRepo)
	tariffRepo(org.powertac.common.repo.TariffRepo)
  timeService(org.powertac.common.TimeService)
}
