package ms;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}