package ms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import scala.Tuple2;
import scala.collection.Seq;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Application.class, args);

    }

//    public void generatedMessages() throws IOException, TimeoutException {
//        List<String> messages = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            messages.add(String.valueOf(i));
//            rabbitMQProducer.inject(messages.get(i));
////            rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", message);
//        }
//
//    }

//    @Bean
//    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setPort(rabbitMQPort);
//        connectionFactory.setHost(rabbitMQHostname);
//        connectionFactory.setUsername(rabbitMQUser);
//        connectionFactory.setPassword(rabbitMQPassword);
////        connectionFactory.setRequestedHeartBeat(20);
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        //The routing key is set to the name of the queue by the broker for the default exchange.
////        template.setRoutingKey(rabbitmqQueueName);
//        template.setExchange(rabbitMQExchange);
//        //Where we will synchronously receive messages from
////        template.setDefaultReceiveQueue(rabbitmqQueueName);
//        return template;
//    }
}