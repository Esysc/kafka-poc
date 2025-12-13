package com.example.claims.model;

import java.time.Instant;

/**
 * Model class representing a claim.
 * <p>
 * This class is not intended for extension.
 */
public final class Claim {
    /** The claim id. */
    private String id;
    /** The patient id. */
    private String patientId;
    /** The claim amount. */
    private Double amount;
    /** The claim status. */
    private String status;
    /** The creation timestamp. */
    private Instant createdAt;

    /**
     * Default constructor.
     */
    public Claim() { }

    /**
     * Constructs a Claim with all fields.
     *
     * @param idParam the claim id
     * @param patientIdParam the patient id
     * @param amountParam the claim amount
     * @param statusParam the claim status
     * @param createdAtParam the creation timestamp
     */
    public Claim(
        final String idParam,
        final String patientIdParam,
        final Double amountParam,
        final String statusParam,
        final Instant createdAtParam
    ) {
        this.id = idParam;
        this.patientId = patientIdParam;
        this.amount = amountParam;
        this.status = statusParam;
        this.createdAt = createdAtParam;
    }

    /**
     * Gets the claim id.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the claim id.
     * @param value the id to set
     */
    public void setId(final String value) {
        this.id = value;
    }

    /**
     * Gets the patient id.
     * @return the patient id
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient id.
     * @param value the patient id to set
     */
    public void setPatientId(final String value) {
        this.patientId = value;
    }

    /**
     * Gets the claim amount.
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the claim amount.
     * @param value the amount to set
     */
    public void setAmount(final Double value) {
        this.amount = value;
    }

    /**
     * Gets the claim status.
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the claim status.
     * @param value the status to set
     */
    public void setStatus(final String value) {
        this.status = value;
    }

    /**
     * Gets the creation timestamp.
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param value the timestamp to set
     */
    public void setCreatedAt(final Instant value) {
        this.createdAt = value;
    }
}
