package com.example.claims.consumer;

import com.example.claims.model.Claim;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for input claims.
 * <p>
 * This class is not intended for extension.
 */
@Component
public class ClaimsInputConsumer {

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
        System.out.println("Received claim: " + claim);
        // Add your processing logic here
    }
}
