package com.nhis.fraud.auditor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nhis.fraud.auditor.entities.ClaimRecord;
import com.nhis.fraud.auditor.repositories.ClaimRepository;
import com.nhis.fraud.auditor.services.ClaimUploadService;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin("*")
public class ClaimController {

    @Autowired
    private ClaimRepository repo;

    @Autowired
    private ClaimUploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
        uploadService.uploadCsv(file);
        return ResponseEntity.ok("Uploaded and scored successfully");
    }

    @GetMapping("/flagged")
    public List<ClaimRecord> getFlagged(@RequestParam(defaultValue = "70") Double threshold) {
        return repo.findByFraudScoreGreaterThan(threshold);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<?> reviewClaim(
            @PathVariable String id,
            @RequestParam String status
    ) {
        ClaimRecord c = repo.findById(id).orElseThrow();
        c.setReviewStatus(status);
        repo.save(c);
        return ResponseEntity.ok("Saved");
    }
}