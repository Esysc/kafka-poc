package com.example.claims.config;

import com.example.claims.avro.Claim;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration for Kafka producers.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public class KafkaProducerConfig {

    /** Kafka bootstrap servers property. */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Schema Registry URL property injected from application.yml.
     */
    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    /**
     * Creates a ProducerFactory for Claim objects.
     *
     * @return a ProducerFactory for Claim
     */
    @Bean
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
            KafkaAvroSerializer.class
        );
        configProps.put(
            "schema.registry.url",
            schemaRegistryUrl
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
