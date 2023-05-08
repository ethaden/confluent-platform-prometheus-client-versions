package io.confluent.csta.examples.security;

import io.confluent.csta.examples.security.admin.TopicCreator;
import io.confluent.csta.examples.security.common.PropertiesLoader;
import io.confluent.csta.examples.security.consumer.EchoConsumer;
import io.confluent.csta.examples.security.producer.EchoProducer;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static final String ECHO_TOPIC = "echo-topic-plain";
    private static final Duration DELAY = Duration.ofSeconds(1);

    public static void main(final String[] args) throws IOException, ExecutionException, InterruptedException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Only one parameter accepted: property file path");
        }

        final Properties properties = PropertiesLoader.load(args[0]);

        final TopicCreator topicCreator = TopicCreator.build(properties);
        topicCreator.create(ECHO_TOPIC);

        final EchoProducer echoProducer = new EchoProducer(properties, ECHO_TOPIC, EchoProducer.NEVER_STOP, DELAY);
        final EchoConsumer echoConsumer = new EchoConsumer(properties, ECHO_TOPIC, DELAY);

        final ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.submit(echoProducer);
        threadPool.submit(echoConsumer);
    }
}
