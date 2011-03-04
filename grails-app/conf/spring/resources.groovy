import org.springframework.jms.connection.SingleConnectionFactory
import org.apache.activemq.ActiveMQConnectionFactory

beans = {
	jmsConnectionFactory(SingleConnectionFactory) {
		targetConnectionFactory = { ActiveMQConnectionFactory cf ->
			brokerURL = ''
		}
	}
}
