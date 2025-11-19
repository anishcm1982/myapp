package com.nhis.fraud.auditor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhis.fraud.auditor.entities.ClaimRecord;

public interface ClaimRepository extends JpaRepository<ClaimRecord, String> {

    List<ClaimRecord> findByFraudScoreGreaterThan(Double threshold);

    List<ClaimRecord> findByProviderIdContainingIgnoreCase(String providerId);

    List<ClaimRecord> findByDuplicateFlag(Integer flag);
}
