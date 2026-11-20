package com.joshua.insureflow.policy.dto;

import com.joshua.insureflow.policy.Policy;
import com.joshua.insureflow.policy.PolicyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PolicyResponse(
    Long id,
    String policyNumber,
    Long customerId ,
    PolicyType policyType,
    LocalDate startDate,
    LocalDate expiryDate,
    BigDecimal premium,
    String status,

    LocalDateTime createdAt
            ){

    public static PolicyResponse fromEntity(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getCustomer().getId(),
                policy.getPolicyType(),
                policy.getStartDate(),
                policy.getExpiryDate(),
                policy.getPremium(),
                policy.getStatus().name(),
                policy.getCreatedAt()
        );
    }
}
