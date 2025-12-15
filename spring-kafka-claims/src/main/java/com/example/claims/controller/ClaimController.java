package com.example.claims.controller;

import com.example.claims.avro.Claim;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import com.example.claims.dto.ClaimDto;
import java.util.stream.Collectors;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.example.claims.consumer.ClaimsInputConsumer;
import org.apache.commons.text.StringEscapeUtils;
/**
 * Controller for claim-related endpoints.
 * <p>
 * This class is not intended for extension.
 */
@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    // Kafka template for sending claims (removed misplaced Javadoc)

    /**
     * Returns the list of received claims (in-memory, for demo/testing only).
     * @return ResponseEntity with the list of ClaimDto
     */
    @GetMapping
    public ResponseEntity<List<ClaimDto>> getClaims() {
        List<ClaimDto> dtos = ClaimsInputConsumer.getReceivedClaims()
            .stream()
            .map(ClaimDto::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    /** Kafka template for sending claims. */
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
     * @param claimDto the claim DTO
     * @return ResponseEntity with status
     * @throws JsonProcessingException if serialization fails
     */
    @PostMapping
    public ResponseEntity<String> postClaim(
        @RequestBody final ClaimDto claimDto)
        throws JsonProcessingException {
        // Convert ClaimDto to Avro Claim
        Claim claim = new Claim();
        claim.setId(claimDto.getId());
        claim.setPatientId(claimDto.getPatientId());
        claim.setAmount(claimDto.getAmount());
        claim.setStatus(claimDto.getStatus());
        claim.setCreatedAt(claimDto.getCreatedAt());

        String claimId = claimDto.getId() != null
            ? claimDto.getId() : "default-claim-id";
        LOG.info("Sending claim of type: {}", claim.getClass().getName());
        LOG.info(
                "KafkaTemplate value serializer: {}",
                kafkaTemplate.getProducerFactory()
                    .getConfigurationProperties()
                    .get("value.serializer")
            );
        kafkaTemplate.send("claims-input", claimId, claim);
        LOG.info("Posted claim with id {}", claimId);
        String responseMessage = "{\"message\": \"Claim posted with id: "
            + StringEscapeUtils.escapeJson(claimId) + "\"}";
        return ResponseEntity.ok(responseMessage);
    }
}
