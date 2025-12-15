package com.example.claims.consumer;

import com.example.claims.avro.Claim;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;


/**
 * Consumer for input claims.
 * <p>
 * This class is not intended for extension.
 */
@Component
public class ClaimsInputConsumer {

    /** Logger for this class. */
    private static final Logger LOG =
        LoggerFactory.getLogger(ClaimsInputConsumer.class);

    /**
     * In-memory list to store received claims.
     * Visible for testing/demo only.
     */
    private static final List<Claim> RECEIVED_CLAIMS =
        new java.util.concurrent.CopyOnWriteArrayList<>();

    /** Timer for claim processing time. */
    private final Timer processingTimer;
    /** Counter for processed claims. */
    private final Counter processedCounter;

    /**
     * Constructor logs bean creation.
     * @param registry the MeterRegistry
     */
    public ClaimsInputConsumer(final MeterRegistry registry) {
        LOG.info(
                "ClaimsInputConsumer bean created and "
                + "KafkaListener should be active."
            );
        this.processingTimer = Timer.builder("claims.input.processing.time")
            .description("Time to process claims-input messages")
            .register(registry);
        this.processedCounter = Counter.builder("claims.input.processed")
            .description("Total claims processed from input topic")
            .register(registry);
    }

    /**
     * Returns the list of received claims (in-memory, for demo/testing only).
     * @return the list of received claims
     */
    public static List<Claim> getReceivedClaims() {
        return RECEIVED_CLAIMS;
    }

    /**
     * Listens for incoming claim messages.
     * @param claim the claim
     */
    @KafkaListener(
        topics = "claims-input",
        groupId = "claims-app",
        containerFactory = "claimKafkaListenerContainerFactory"
    )
    public void listen(final Claim claim) {
        processingTimer.record(() -> {
            LOG.debug(
                "Kafka consumer triggered for claims-input topic"
            );
            if (claim == null) {
                LOG.warn("Received null claim message");
                return;
            }
            LOG.info("Received claim: {}", claim);
            RECEIVED_CLAIMS.add(claim);
            processedCounter.increment();
            // Add your processing logic here
        });
    }
}
