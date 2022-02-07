package fr.viteducode.kafkaclient.eventhub;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerApplication {

    public static void main(String[] args) {

        Properties properties = new Properties();

        String connectionString = "Endpoint=sb://eventhub-viteducode.servicebus.windows.net/;SharedAccessKeyName=eh-consumer-policy;SharedAccessKey=<key>";

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "eventhub-viteducode.servicebus.windows.net:9093");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"" + connectionString + "\";");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer1");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singleton("simple_topic"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Message key : %s - Message value : %s - Message offset : %d - Message partition : %d\n",
                            record.key(), record.value(), record.offset(), record.partition());
                }
                kafkaConsumer.commitAsync();
            }
        } finally {
            kafkaConsumer.close();
        }
    }
}