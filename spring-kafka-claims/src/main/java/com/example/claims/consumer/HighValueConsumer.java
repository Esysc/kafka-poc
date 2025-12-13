package com.example.claims.consumer;

import com.example.claims.model.Claim;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for high-value claims.
 * <p>
 * This class is not intended for extension.
 */
@Component
public class HighValueConsumer {
    /** Logger for this class. */
        private static final Logger LOG =
                LoggerFactory.getLogger(HighValueConsumer.class);
    /** Counter for high-value claims processed. */
    private final Counter highValueCounter;

    /**
     * Constructs a HighValueConsumer.
     * @param registry the MeterRegistry
     */
    public HighValueConsumer(final MeterRegistry registry) {
        this.highValueCounter = Counter.builder("claims.highvalue.processed")
            .description("High value claims processed")
            .register(registry);
    }

    /**
     * Handles high-value claim messages.
     * @param claim the claim
     */
    @KafkaListener(topics = "claims-highvalue", groupId = "highvalue-consumer")
    public void handle(final Claim claim) {
        if (claim == null) {
            return;
        }
        highValueCounter.increment();
        LOG.info(
            "High-value claim alert: id={}, patient={}, amount={}",
            claim.getId(),
            claim.getPatientId(),
            claim.getAmount()
        );
    }
}
