package fr.viteducode.springkafka.eventhub;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "topic_spring")
    public void listen(String in) {
        System.out.println(in);
    }
}
