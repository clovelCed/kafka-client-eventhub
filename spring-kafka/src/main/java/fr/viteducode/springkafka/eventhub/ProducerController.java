package fr.viteducode.springkafka.eventhub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

@Controller
public class ProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/produce/{value}")
    public ResponseEntity<String> produce(@PathVariable String value) {
        SendResult<String, String> result;
        try {
            result = this.kafkaTemplate.send("topic_spring", value).get();
            return ResponseEntity.ok("Produced on offset : " + result.getRecordMetadata().offset());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        return ResponseEntity.internalServerError().build();
    }
}
