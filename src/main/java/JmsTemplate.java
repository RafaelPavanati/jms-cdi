import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Rafael Pavanati
 */
@ApplicationScoped
public class JmsTemplate {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JmsTemplate.class);

    @Inject
    private ActiveMQConnectionFactory connectionFactory;


    public void send(String destination, Serializable message) {

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {

            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(destination);
            final ObjectMessage jmsMessage = session.createObjectMessage(message);
            producer = send(queue, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }


    public void send(String destination, Map<String, Object> message) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(destination);
            final MapMessage jmsMessage = session.createMapMessage();

            for (Map.Entry<String, Object> entry : message.entrySet()) {
                jmsMessage.setObject(entry.getKey(), entry.getValue());
            }

            producer = send(queue, session, producer, jmsMessage);
        } catch (JMSException e) {
            rollback(session, e);
        } finally {
            close(connection, session, producer);
        }
    }


    private MessageProducer send(Destination d, Session session, MessageProducer producer, Message textMessage) throws JMSException {
        producer = session.createProducer(d);
        producer.send(textMessage);
        session.commit();
        return producer;
    }

    private void close(Connection connection, Session session, MessageProducer producer) {
        if (connection != null) {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOGGER.error("Erro :", e);
            }
        }

        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                LOGGER.error("Erro :", e);
            }
        }

        if (producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                LOGGER.error("Erro :", e);
            }
        }
    }

    private void rollback(Session session, JMSException e) {
        if (session != null) {
            try {
                session.rollback();
            } catch (JMSException e1) {
                LOGGER.error("Erro :", e);
            }
        }
    }
}
