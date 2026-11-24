package com.joshua.insureflow.claim.dto;

import com.joshua.insureflow.claim.Claim;
import com.joshua.insureflow.claim.ClaimStatus;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClaimResponse(
        Long id,
        Long policyId,
        String description,

        String rejectionReason,
        ClaimStatus status,
        LocalDateTime submittedAt,
        LocalDateTime decidedAt,
        LocalDate incidentDate,
        BigDecimal claimAmount
        ){

        public static ClaimResponse fromEntity(Claim claim) {
                return new ClaimResponse(
                        claim.getId(),
                        claim.getPolicy().getId(),
                        claim.getDescription(),
                        claim.getRejectionReason(),
                        claim.getStatus(),
                        claim.getSubmittedAt(),
                        claim.getDecidedAt(),
                        claim.getIncidentDate(),
                        claim.getClaimAmount()
                );
        }
}
