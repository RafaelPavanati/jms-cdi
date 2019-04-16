# ActiveMQ

Anotação para uso de JMS usando CDI 


Primeiramente precisamos definir uma factory 

```java

@ApplicationScoped
public class ConectionFactory{
    
        @Produces
        public ActiveMQConnectionFactory conectionFactory() {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL("URLBROKER:PORTA");
            connectionFactory.setUserName("USUARIO");
            connectionFactory.setPassword("SENHA");
        }
}

``` 

Para Consumir a menssagem basta usar a anotação `@JmsListener` com o nome da fila

```java

@ApplicationScoped
@JmsListener(destination = "nome-fila")
public class Consumer implements MessageListener {
    
     @Override
        public void onMessage(Message message) {
          /**
          * 
           */
        }
}
```

Para enviar uma menssagem injetamos `JmsTemplate` e usamos o metodo send , passando o nome da fila como primeiro parametro.
```java

@Inject
JmsTemplate jmsTemplate;


public void enviar(Object object){
    jmsTemplate.send("nome-fila", object);
}

```