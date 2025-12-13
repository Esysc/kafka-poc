package com.example.claims.controller;

import com.example.claims.model.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for claim-related endpoints.
 * <p>
 * This class is not intended for extension.
 */
@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    /**
     * Kafka template for sending claims.
     */
    private final KafkaTemplate<String, Claim> kafkaTemplate;
    /** Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(
        ClaimController.class
    );

    /**
     * Constructs a ClaimController.
     * @param kafkaTemplateParam the KafkaTemplate
     */
    public ClaimController(
        final KafkaTemplate<String, Claim> kafkaTemplateParam) {
        this.kafkaTemplate = kafkaTemplateParam;
    }

    /**
     * Posts a claim to the Kafka topic.
     * @param claim the claim
     * @return ResponseEntity with status
     * @throws JsonProcessingException if serialization fails
     */
    @PostMapping
    public ResponseEntity<String> postClaim(@RequestBody final Claim claim)
        throws JsonProcessingException {
        String claimId = claim.getId();
        if (claimId == null) {
            claimId = "default-claim-id";
        }
        kafkaTemplate.send("claims-input", claimId, claim);
        LOG.info(
            "Posted claim with id {}",
            claimId
        );
        return ResponseEntity.ok(
            "Claim posted with id: " + claimId
        );
    }
}
