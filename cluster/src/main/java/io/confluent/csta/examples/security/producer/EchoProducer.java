package io.confluent.csta.examples.security.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

public class EchoProducer implements Runnable {

    public static final int NEVER_STOP = -1;
    private static final Logger LOG = LoggerFactory.getLogger(EchoProducer.class);
    private final KafkaProducer<String, String> producer;
    private final Duration delay;
    private final String topic;
    private final int repetition;

    public EchoProducer(final Properties config, final String topic, final int repetition, final Duration delay) {
        this.producer = new KafkaProducer<>(config);
        this.topic = topic;
        this.repetition = repetition;
        this.delay = delay;

        // Adding a shutdown hook to clean up when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Closing echo producer for topic [{}]", topic);
            producer.close();
        }));
    }

    @Override
    public void run() {
        int sentMessages = 0;
        while (shouldSendMessages(sentMessages)) {
            final String message = generatePingMessage();
            final ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, message);
            LOG.info("Sending message [{}] sent to topic [{}]", record.value(), record.topic());
            producer.send(record, this::logMessageSent);
            waitUntilDurationExpires();
            sentMessages++;
        }
    }

    private void waitUntilDurationExpires() {
        try {
            Thread.sleep(delay.toMillis());
        } catch (final InterruptedException e) {
            LOG.error("Ops, sleep was interruped!", e);
        }
    }

    private void logMessageSent(final RecordMetadata metadata, final Exception exception) {
        if (exception != null) {
            LOG.error("Error sending message to topic [{}]", metadata.topic(), exception);
        } else {
            LOG.debug("Message acknowledged to topic [{}]", metadata.topic());
        }
    }

    private String generatePingMessage() {
        return "Generated at " + LocalDateTime.now();
    }

    private boolean shouldSendMessages(final int sentMessages) {
        return repetition == NEVER_STOP || sentMessages >= repetition;
    }
}
