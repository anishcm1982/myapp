package com.nhis.fraud.auditor.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhis.fraud.auditor.entities.ClaimRecord;

@Service
public class FraudScoringEngine {

    public void applyFraudScores(List<ClaimRecord> claims) {

        double mean = claims.stream()
                .mapToDouble(ClaimRecord::getClaimAmount)
                .average().orElse(0);

        double variance = claims.stream()
                .mapToDouble(c -> Math.pow(c.getClaimAmount() - mean, 2))
                .sum() / Math.max(1, claims.size() - 1);

        double std = Math.sqrt(variance);

        // Provider volume
        Map<String, Long> providerCounts = claims.stream()
                .collect(Collectors.groupingBy(ClaimRecord::getProviderId, Collectors.counting()));

        long minProv = providerCounts.values().stream().min(Long::compare).orElse(0L);
        long maxProv = providerCounts.values().stream().max(Long::compare).orElse(1L);

        // Duplicate detection
        Map<String, Long> dupCounts = claims.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getPatientId() + "|" + c.getServiceDate() + "|" + c.getClaimAmount(),
                        Collectors.counting()
                ));

        for (ClaimRecord c : claims) {
            // Amount Z-score
            double z = (c.getClaimAmount() - mean) / (std == 0 ? 1 : std);
            double zPos = Math.max(0, z);
            double amountScore = Math.min(1.0, zPos / 3.0);

            double providerNorm = (providerCounts.get(c.getProviderId()) - minProv) /
                    (double)(maxProv - minProv);

            int dupFlag = dupCounts.getOrDefault(
                    c.getPatientId() + "|" + c.getServiceDate() + "|" + c.getClaimAmount(),
                    0L
            ) > 1 ? 1 : 0;

            double rawScore = (0.5 * amountScore) + (0.3 * providerNorm) + (0.2 * dupFlag);
            double fraudScore = Math.round(rawScore * 1000) / 10.0;

            c.setAmountZScore(zPos);
            c.setProviderVolumeNorm(providerNorm);
            c.setDuplicateFlag(dupFlag);
            c.setFraudScore(fraudScore);
        }
    }
}
