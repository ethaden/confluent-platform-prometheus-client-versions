package io.confluent.developer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaTransactionalProducerApplication {


    public static Properties loadProperties(String fileName) throws IOException {
        final Properties envProps = new Properties();
        final FileInputStream input = new FileInputStream(fileName);
        envProps.load(input);
        input.close();
        return envProps;
    }

    public void printMetadata(final Collection<Future<RecordMetadata>> metadata,
                              final String fileName) {
        System.out.println("Offsets and timestamps committed in batch from " + fileName);
        metadata.forEach(m -> {
            try {
                final RecordMetadata recordMetadata = m.get();
                System.out.println("Record written to offset " + recordMetadata.offset() + " timestamp " + recordMetadata.timestamp());
            } catch (InterruptedException | ExecutionException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        final Properties props = KafkaAvroProducerApplication.loadProperties(args[0]);
        final String topic = props.getProperty("output.topic.name");
        final Producer<String, String> producer = new KafkaProducer<String, String>(props);

        producer.initTransactions();
        producer.beginTransaction();
        producer.send(new ProducerRecord<String, String>(topic, "value1"));
        producer.flush();
        producer.send(new ProducerRecord<String, String>(topic, "value2"));
        producer.flush();
        producer.send(new ProducerRecord<String, String>(topic, "value3"));
        producer.flush();
        producer.commitTransaction();
    }
}