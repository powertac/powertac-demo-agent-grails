beans = {
  jmsConnectionFactory(org.apache.activemq.pool.PooledConnectionFactory) {bean ->
    bean.destroyMethod = "stop"
    connectionFactory = {org.apache.activemq.ActiveMQConnectionFactory cf ->
      brokerURL = ''
    }
  }

  messageConverter(org.powertac.common.MessageConverter) { bean ->
    bean.initMethod = 'afterPropertiesSet'
  }
  messageListenerRegistrar(org.powertac.broker.infrastructure.messaging.MessageListenerRegistrar) {}
  xmlMessageReceiver(org.powertac.broker.infrastructure.messaging.XMLMessageReceiver) {
    messageListenerRegistrar = messageListenerRegistrar
    messageConverter = messageConverter
  }
  messageReceiver(org.powertac.broker.infrastructure.messaging.MessageReceiver) {
    xmlMessageReceiver = xmlMessageReceiver
  }

  arrayListProcessor(org.powertac.broker.infrastructure.messaging.ArrayListProcessor) { bean ->
    bean.initMethod = 'initialize'
    messageListenerRegistrar = messageListenerRegistrar
  }

  tariffNegotiator(org.powertac.broker.core.tariffnegotiator.DemoTariffNegotiator) { bean ->
    bean.initMethod = 'initialize'
    messageListenerRegistrar = messageListenerRegistrar
  }
}
