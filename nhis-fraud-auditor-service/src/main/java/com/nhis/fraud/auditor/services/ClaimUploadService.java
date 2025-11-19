package com.nhis.fraud.auditor.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nhis.fraud.auditor.entities.ClaimRecord;
import com.nhis.fraud.auditor.repositories.ClaimRepository;

@Service
public class ClaimUploadService {

    @Autowired
    private ClaimRepository repo;

    @Autowired
    private FraudScoringEngine scoringEngine;

    public void uploadCsv(MultipartFile file) throws Exception {

        List<ClaimRecord> claims = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                ClaimRecord c = new ClaimRecord();
                c.setClaimId(row[0]);
                c.setProviderId(row[1]);
                c.setPatientId(row[2]);
                c.setServiceDate(LocalDate.parse(row[3]));
                c.setClaimAmount(Double.valueOf(row[4]));

                claims.add(c);
            }
        }

        scoringEngine.applyFraudScores(claims);
        repo.saveAll(claims);
    }
}