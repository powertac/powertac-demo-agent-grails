
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

	tariffNegotiator(org.powertac.broker.core.tariffnegotiator.DemoTariffNegotiator)
	springApplicationContext(org.powertac.common.spring.SpringApplicationContext)
	messageConverter(org.powertac.common.XMLMessageConverter)
	brokerRepo(org.powertac.common.repo.BrokerRepo)
	customerRepo(org.powertac.common.repo.CustomerRepo)
	timeslotRepo(org.powertac.common.repo.TimeslotRepo)
	tariffRepo(org.powertac.common.repo.TariffRepo)
	timeService(org.powertac.common.TimeService)
	brokerRepo(org.powertac.common.repo.BrokerRepo)
	customerRepo(org.powertac.common.repo.CustomerRepo)
	orderbookRepo(org.powertac.common.repo.OrderbookRepo)
	tariffRepo(org.powertac.common.repo.TariffRepo)
	timeslotRepo(org.powertac.common.repo.TimeslotRepo)
	weatherForecastRepo(org.powertac.common.repo.WeatherForecastRepo)
	weatherReportRepo(org.powertac.common.repo.WeatherReportRepo)
  messageListenerRegistrar(org.powertac.broker.infrastructure.messaging.MessageListenerRegistrar)

  xmlMessageReceiver(org.powertac.broker.infrastructure.messaging.XMLMessageReceiver) {
    messageListenerRegistrar = messageListenerRegistrar
    messageConverter = messageConverter
  }

  messageReceiver(org.powertac.broker.infrastructure.messaging.MessageReceiver) {
    xmlMessageReceiver = xmlMessageReceiver
  }

  arrayListProcessor(org.powertac.broker.infrastructure.messaging.ArrayListProcessor) { bean ->
    messageListenerRegistrar = messageListenerRegistrar
  }

  messagePersistenceManager(org.powertac.broker.infrastructure.persistence.RepoBasedMessagePersistenceManager) { bean ->
		brokerRepo = brokerRepo
	  customerRepo = customerRepo
	  orderbookRepo = orderbookRepo
	  tariffRepo = tariffRepo
		timeslotRepo = timeslotRepo
	  weatherForecastRepo = weatherForecastRepo
	  weatherReportRepo = weatherReportRepo
  }

  marketMessageListener(org.powertac.broker.infrastructure.messaging.MarketMessageListener)  { bean ->
    messagePersistenceManager = messagePersistenceManager
  }
}
