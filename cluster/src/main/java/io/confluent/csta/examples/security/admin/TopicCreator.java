package io.confluent.csta.examples.security.admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TopicCreator {

    private static final Logger LOG = LoggerFactory.getLogger(TopicCreator.class);
    private static final int DEFAULT_NUM_PARTITIONS = 3;
    private static final short DEFAULT_REPLICATION_FACTOR = 1;
    private final Properties properties;

    private TopicCreator(final Properties properties) {
        this.properties = properties;
    }

    public static TopicCreator build(final Properties properties) {
        return new TopicCreator(properties);
    }

    public void create(final String topic) throws ExecutionException, InterruptedException {
        final NewTopic newTopic = new NewTopic(topic, DEFAULT_NUM_PARTITIONS, DEFAULT_REPLICATION_FACTOR);
        try (final AdminClient adminClient = AdminClient.create(properties)) {
            adminClient.createTopics(Collections.singletonList(newTopic))
                    .all()
                    .get();
        } catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (e.getCause() instanceof TopicExistsException) {
                LOG.info("Topic [" + topic + "] already exists, skipping creation.");
            } else {
                LOG.error("Ops, we failed.");
                throw e;
            }
        }
    }
}
