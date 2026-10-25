package com.joshua.insureflow.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        LocalDateTime createdAt
) {
}