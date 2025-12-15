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
     * Default constructor for JSON deserialization.
     */
    public ClaimDto() {
    }

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

    /** @param idParam the claim id */
    public void setId(final String idParam) {
        this.id = idParam;
    }

    /** @return the patient id */
    public String getPatientId() {
        return patientId;
    }

    /** @param patientIdParam the patient id */
    public void setPatientId(final String patientIdParam) {
        this.patientId = patientIdParam;
    }

    /** @return the claim amount */
    public Double getAmount() {
        return amount;
    }

    /** @param amountParam the claim amount */
    public void setAmount(final Double amountParam) {
        this.amount = amountParam;
    }

    /** @return the claim status */
    public String getStatus() {
        return status;
    }

    /** @param statusParam the claim status */
    public void setStatus(final String statusParam) {
        this.status = statusParam;
    }

    /** @return the creation timestamp */
    public String getCreatedAt() {
        return createdAt;
    }

    /** @param createdAtParam the creation timestamp */
    public void setCreatedAt(final String createdAtParam) {
        this.createdAt = createdAtParam;
    }
}
