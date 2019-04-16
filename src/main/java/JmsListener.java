import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.beans.ExceptionListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rafael Pavanati
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface JmsListener {
    @Nonbinding Session session() default Session.AUTO_ACKNOWLEDGE;
    @Nonbinding DestinationType type() default DestinationType.QUEUE;
    @Nonbinding String destination();
    @Nonbinding Class<? extends ExceptionListener> exceptionListener() default ExceptionListener.class;

    enum DestinationType {
        QUEUE, TOPIC;
    }

    enum Session {
        AUTO_ACKNOWLEDGE(1),
        CLIENT_ACKNOWLEDGE(2),
        DUPS_OK_ACKNOWLEDGE(3),
        SESSION_TRANSACTED(0);

        int value;

        Session(int value) {
            this.value = value;
        }
    }
}
