package com.example.claims.streams;

import com.example.claims.model.Claim;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.StreamsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Instant;
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
        JsonSerde<Claim> serde = new JsonSerde<>(Claim.class);
        TestInputTopic<String, Claim> input = testDriver.createInputTopic("claims-input", Serdes.String().serializer(), serde.serializer());
        TestOutputTopic<String, Claim> output = testDriver.createOutputTopic("claims-highvalue", Serdes.String().deserializer(), serde.deserializer());

        Claim low = new Claim("c1","p1",500.0,"NEW", Instant.now());
        Claim high = new Claim("c2","p2",1500.0,"NEW", Instant.now());

        input.pipeInput(low.getId(), low);
        input.pipeInput(high.getId(), high);

        assertTrue(output.isEmpty() == false);
        Claim out = output.readValue();
        assertEquals("c2", out.getId());
    }
}
