package com.example.claims.config;

import com.example.claims.avro.Claim;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.CommonErrorHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;


/**
 * Configuration for Kafka consumers.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public class KafkaConsumerConfig {

    /** Kafka bootstrap servers property. */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Schema Registry URL property injected from application.yml.
     */
    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    private String schemaRegistryUrl;

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
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            KafkaAvroDeserializer.class);
        props.put("specific.avro.reader", true);
        props.put("schema.registry.url", schemaRegistryUrl);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Creates a KafkaListenerContainerFactory for Claim objects.
     * <p>
     * If extending this class, override this method with caution.
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
        factory.setCommonErrorHandler(commonErrorHandler());
        return factory;
    }

    /**
     * Alias bean for default KafkaListenerContainerFactory name.
     * <p>
     * If extending this class, override this method with caution.
     *
     * @return the default KafkaListenerContainerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?>
        kafkaListenerContainerFactory() {
            return
                claimKafkaListenerContainerFactory();
        }

    /**
     * Provides a CommonErrorHandler for Kafka consumers.
     * <p>
     * If extending this class, override this method with caution.
     *
     * @return a CommonErrorHandler instance
     */
    @Bean
    public CommonErrorHandler commonErrorHandler() {
        return new DefaultErrorHandler((thrownException, record) -> {
                System.err.println(
                    "Error in process with Exception "
                    + thrownException
                    + ", record: "
                    + record
                );
        });
    }
}
