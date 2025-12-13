package com.example.claims.config;

import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Kafka Streams.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public final class KafkaStreamsConfig {
    /**
     * Provides a StreamsBuilder bean for Kafka Streams.
     *
     * @return a new StreamsBuilder instance
     */
    @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }
}
