package com.example.claims.dto;

import com.example.claims.avro.Claim;

/**
 * Data Transfer Object for Claim.
 */
public class ClaimDto {
    /** The claim id. */
    private String id;
    /** The patient id. */
    private String patientId;
    /** The claim amount. */
    private Double amount;
    /** The claim status. */
    private String status;
    /** The creation timestamp. */
    private String createdAt;

    /**
     * Constructs a ClaimDto from an Avro Claim.
     * @param claim the Avro claim
     */
    public ClaimDto(final Claim claim) {
        this.id = (claim.getId() != null)
            ? claim.getId().toString() : null;
        this.patientId = (claim.getPatientId() != null)
            ? claim.getPatientId().toString() : null;
        this.amount = claim.getAmount();
        this.status = (claim.getStatus() != null)
            ? claim.getStatus().toString() : null;
        this.createdAt = (claim.getCreatedAt() != null)
            ? claim.getCreatedAt().toString() : null;
    }

    /** @return the claim id */
    public String getId() {
        return id;
    }

    /** @return the patient id */
    public String getPatientId() {
        return patientId;
    }

    /** @return the claim amount */
    public Double getAmount() {
        return amount;
    }

    /** @return the claim status */
    public String getStatus() {
        return status;
    }

    /** @return the creation timestamp */
    public String getCreatedAt() {
        return createdAt;
    }
}
