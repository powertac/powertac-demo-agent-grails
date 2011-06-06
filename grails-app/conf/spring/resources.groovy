
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

  messageConverter(org.powertac.common.MessageConverter)

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

  tariffNegotiator(org.powertac.broker.core.tariffnegotiator.DemoTariffNegotiator) { bean ->
    messageListenerRegistrar = messageListenerRegistrar
  }
}
