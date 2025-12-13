package com.example.claims.config;

import com.example.claims.model.Claim;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration for Kafka producers.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public final class KafkaProducerConfig {

    /** Kafka bootstrap servers property. */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates a ProducerFactory for Claim objects.
     *
     * @return a ProducerFactory for Claim
     */
    @Bean
    @org.springframework.lang.NonNull
    public ProducerFactory<String, Claim> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
        configProps.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class
        );
        configProps.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate for Claim objects.
     *
     * @return a KafkaTemplate for Claim
     */
    @Bean
    public KafkaTemplate<String, Claim> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
