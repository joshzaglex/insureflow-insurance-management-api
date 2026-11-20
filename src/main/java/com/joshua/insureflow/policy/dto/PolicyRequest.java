package com.joshua.insureflow.policy.dto;



import com.joshua.insureflow.policy.PolicyType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyRequest(

    @NotNull(message = "Customer id is required")
     Long customerId,

    @NotNull(message = "Policy Type is required")
    PolicyType policyType,

    @NotNull(message = "Start Date is required")
     LocalDate startDate,


    @NotNull(message = "Expiry Date is required")
     LocalDate expiryDate,

    @NotNull(message = "Premium is required")
            @Positive(message = "Premium must be a positive value")
     BigDecimal premium
){

    @AssertTrue(message = "Expiry date must be after start date")
    public boolean isExpiryAfterStart() {
        if (startDate == null || expiryDate == null) {
            return true;
        }
        return expiryDate.isAfter(startDate);
    }




}