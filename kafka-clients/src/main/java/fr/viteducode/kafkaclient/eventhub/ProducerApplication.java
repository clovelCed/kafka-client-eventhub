package fr.viteducode.kafkaclient.eventhub;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerApplication {

    public static void main(String[] args) {

        Properties properties = new Properties();

        String connectionString = "Endpoint=sb://eventhub-viteducode.servicebus.windows.net/;SharedAccessKeyName=eh-producer-policy;SharedAccessKey=<key>";

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "eventhub-viteducode.servicebus.windows.net:9093");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"" + connectionString + "\";");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "simple_topic";
        String key = "10";
        String value = "John";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, (recordMetadata, exception) -> {
            if (exception != null) {
                System.out.println(exception.getMessage());
            }
        });

        producer.close();

    }
}
