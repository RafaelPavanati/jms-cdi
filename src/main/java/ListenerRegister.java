import org.apache.activemq.ActiveMQConnectionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.NamingException;

/**
 * @author Rafael Pavanati rafapavanati@gmail.com
 */
@ApplicationScoped
public class ListenerRegister {

    @Inject
    @JmsListener(destination = "")
    private Instance<MessageListener> messageListeners;

    @Inject
    @Any
    private Instance<ExceptionListener> exceptionListeners;

    @Inject
    private ActiveMQConnectionFactory connectionFactory;



    public void register() throws JMSException {
        for (MessageListener messageListener : messageListeners) {
            JmsListener jmsListener = messageListener.getClass().getAnnotation(JmsListener.class);

            if (jmsListener == null) {
                jmsListener = messageListener.getClass().getSuperclass().getAnnotation(JmsListener.class);
            }

            Connection connection = connectionFactory.createConnection();

            final Session session = connection.createSession(false, jmsListener.session().value);
            Queue queue = session.createQueue(jmsListener.destination());

            final MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(messageListener);

            connection.start();
        }
    }
}
