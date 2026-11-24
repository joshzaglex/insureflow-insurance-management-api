package com.joshua.insureflow.claim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateClaimRequest(

        @NotNull(message = "claim Amount is required")
        @Positive(message = "claim Amount must be a positive value")
        BigDecimal claimAmount,
        @NotBlank(message = "description is required")
        String description,
        @NotNull(message = "incidentDate is required")
        LocalDate incidentDate

) {


}
