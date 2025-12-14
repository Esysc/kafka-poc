package com.example.claims.streams;

import com.example.claims.avro.Claim;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.StreamsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ClaimsStreamTest {
    private TopologyTestDriver testDriver;

    @BeforeEach
    public void setup() {
        StreamsBuilder builder = new StreamsBuilder();
        new ClaimsStreamProcessor().kStream(builder);
        Topology topology = builder.build();

        Properties props = new Properties();
        props.put("bootstrap.servers", "dummy:9092");
        props.put("application.id", "test");

        testDriver = new TopologyTestDriver(topology, props);
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void testHighValueFiltered() {
        try (SpecificAvroSerde<Claim> serde = new SpecificAvroSerde<>()) {
            serde.configure(java.util.Collections.singletonMap("schema.registry.url", "mock://test"), false);

            TestInputTopic<String, Claim> input = testDriver.createInputTopic(
                "claims-input",
                Serdes.String().serializer(),
                serde.serializer()
            );
            TestOutputTopic<String, Claim> output = testDriver.createOutputTopic(
                "claims-highvalue",
                Serdes.String().deserializer(),
                serde.deserializer()
            );

            Claim low = Claim.newBuilder()
                .setId("c1")
                .setPatientId("p1")
                .setAmount(500.0)
                .setStatus("NEW")
                .setCreatedAt(java.time.Instant.now().toString())
                .build();
            Claim high = Claim.newBuilder()
                .setId("c2")
                .setPatientId("p2")
                .setAmount(1500.0)
                .setStatus("NEW")
                .setCreatedAt(java.time.Instant.now().toString())
                .build();

            input.pipeInput(low.getId().toString(), low);
            input.pipeInput(high.getId().toString(), high);

            assertFalse(output.isEmpty());
            Claim out = output.readValue();
            assertEquals("c2", out.getId().toString());
        }
    }
}
