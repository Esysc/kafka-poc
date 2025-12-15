
package com.example.claims.streams;

import com.example.claims.avro.Claim;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.StreamsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.Map;
import java.util.Collections;

/**
 * Kafka Streams processor for claims.
 * <p>
 * This class is not intended for extension.
 */
@Configuration
public class ClaimsStreamProcessor {
    /** Logger for this class. */
    private static final Logger LOG =
        LoggerFactory.getLogger(ClaimsStreamProcessor.class);
    /** Threshold for high-value claims. */
    private static final double HIGH_VALUE_THRESHOLD = 1000.0;
    /** Window size in minutes for aggregation. */
    private static final long WINDOW_MINUTES = 1L;

    /** Schema registry URL from configuration. */
    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    /**
     * Defines the main Kafka Streams processing pipeline for claims.
     *
     * @param builder the StreamsBuilder
     * @return the main KStream for claims
     */
    @Bean
    public KStream<String, Claim> kStream(final StreamsBuilder builder) {
        final SpecificAvroSerde<Claim> claimSerde = new SpecificAvroSerde<>();
        // Configure schema registry URL for Avro Serde
        Map<String, String> serdeConfig =
            Collections.singletonMap(
                "schema.registry.url",
                schemaRegistryUrl
            );
        claimSerde.configure(serdeConfig, false);

        final KStream<String, Claim> input = builder.stream(
            "claims-input",
            Consumed.with(Serdes.String(), claimSerde)
        );

        // Filter high-value claims > HIGH_VALUE_THRESHOLD
        // and forward raw claim to claims-highvalue
        final KStream<String, Claim> high = input.filter(
            (k, v) ->
                v != null
                && v.getAmount() > HIGH_VALUE_THRESHOLD
        );
        high.to(
            "claims-highvalue",
            Produced.with(Serdes.String(), claimSerde)
        );

        // Example aggregation: sum amounts per patient over tumbling window
        high.map((k, v) ->
                KeyValue.pair(v.getPatientId().toString(), v.getAmount())
            )
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()))
            .windowedBy(
                TimeWindows.ofSizeWithNoGrace(
                    Duration.ofMinutes(WINDOW_MINUTES)
                )
            )
            .reduce(
                (agg, value) ->
                    (agg == null ? 0.0 : agg)
                        + (value == null ? 0.0 : value),
                Materialized.with(Serdes.String(), Serdes.Double())
            )
            .toStream()
            .foreach(
                (windowedKey, total) ->
                    LOG.info(
                        "Patient {} total high-value claims in window {} = {}",
                        windowedKey.key(),
                        windowedKey.window(),
                        total
                    )
            );

        return input;
    }
}
