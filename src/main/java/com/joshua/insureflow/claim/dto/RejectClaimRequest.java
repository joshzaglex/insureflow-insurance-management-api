package com.joshua.insureflow.claim.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectClaimRequest(
        @NotBlank(message = "reason is required")
        String reason) {
}
