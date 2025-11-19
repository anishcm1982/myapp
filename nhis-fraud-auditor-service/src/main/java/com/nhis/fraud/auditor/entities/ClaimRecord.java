package com.nhis.fraud.auditor.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "claims")
public class ClaimRecord {

    @Id
    private String claimId;

    private String providerId;
    private String patientId;
    private LocalDate serviceDate;
    private Double claimAmount;

    // Computed fields
    private Double amountZScore;
    private Double providerVolumeNorm;
    private Integer duplicateFlag;
    private Double fraudScore;
    private String reviewStatus; // Fraud / Not Fraud / Needs Review

    // Getters + Setters omitted for brevity
    public void setProviderVolumeNorm(Double value) {
        if (value == null || value.isNaN() || value.isInfinite()) {
            this.providerVolumeNorm = null;   // or 0.0
        } else {
            this.providerVolumeNorm = value;
        }
    }
}