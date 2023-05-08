package io.confluent.csta.examples.security.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class EchoConsumer implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(EchoConsumer.class);
    private final KafkaConsumer consumer;
    private final String topic;
    private final Duration timeout;

    public EchoConsumer(final Properties properties, final String topic, final Duration timeout) {
        this.consumer = new KafkaConsumer<>(properties);
        this.topic = topic;
        this.timeout = timeout;
    }


    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(topic));
            while (true) {
                final ConsumerRecords<String, String> records = consumer.poll(timeout);
                for (final ConsumerRecord<String, String> record : records) {
                    LOG.info("value = [{}], timestamp = [{}], partition = [{}]",
                            record.value(), record.timestamp(), record.partition());
                }
            }
        } finally {
            LOG.info("Closing echo consumer for topic [{}]", topic);
            consumer.close();
        }
    }

    public void close() {
        LOG.info("Waking up echo consumer for topic [{}]", topic);
        consumer.wakeup();
    }
}
