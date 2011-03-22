import org.springframework.jms.connection.SingleConnectionFactory
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.jms.listener.adapter.MessageListenerAdapter

String brokerDestination = ConfigurationHolder.config.powertac.server
String user = ConfigurationHolder.config.powertac.username
String pass = ConfigurationHolder.config.powertac.apiKey

beans = {
  jmsConnectionFactory(org.apache.activemq.pool.PooledConnectionFactory) {bean ->
    bean.destroyMethod = "stop"
    connectionFactory = {org.apache.activemq.ActiveMQConnectionFactory cf ->
      clientID = user
      brokerURL = brokerDestination
      userName = user
      password = pass
    }
  }
}
