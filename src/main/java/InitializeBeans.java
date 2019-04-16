import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.naming.NamingException;

@Named
@ApplicationScoped
public class InitializeBeans {
    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object o, ListenerRegister listenerRegister) throws JMSException, NamingException {
        listenerRegister.register();
    }
}