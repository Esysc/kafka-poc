package com.example.claims.config;

import com.example.claims.model.Claim;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;


/**
 * Configuration for Kafka consumers.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public final class KafkaConsumerConfig {

    /** Kafka bootstrap servers property. */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates a ConsumerFactory for Claim objects.
     *
     * @return a ConsumerFactory for Claim
     */
    @Bean
    public ConsumerFactory<String, Claim> claimConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "claims-app");
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            ErrorHandlingDeserializer.class
        );
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            ErrorHandlingDeserializer.class
        );
        props.put(
            "spring.deserializer.key.delegate.class",
            StringDeserializer.class
        );
        props.put(
            "spring.deserializer.value.delegate.class",
            JsonDeserializer.class
        );
        props.put(
            "spring.json.trusted.packages",
            "*"
        );
        return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new JsonDeserializer<>(
                Claim.class,
                false
            )
        );
    }

    /**
     * Creates a KafkaListenerContainerFactory for Claim objects.
     *
     * @return a ConcurrentKafkaListenerContainerFactory for Claim
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Claim>
        claimKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Claim> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
            (ConsumerFactory<? super String, ? super Claim>)
                claimConsumerFactory()
        );
        return factory;
    }
}
