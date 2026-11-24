package com.joshua.insureflow.claim;

import com.joshua.insureflow.claim.dto.ClaimResponse;
import com.joshua.insureflow.claim.dto.CreateClaimRequest;
import com.joshua.insureflow.claim.dto.RejectClaimRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimController {

    private final ClaimService claimService;


    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/policies/{policyId}/claims")
    public ResponseEntity<ClaimResponse> submitClaim(@PathVariable Long policyId,@Valid @RequestBody CreateClaimRequest request) {
        ClaimResponse claimResponse = claimService.submitClaim(policyId, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/claims/{id}")
                .buildAndExpand(claimResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(claimResponse);
    }

    @GetMapping("/claims/{id}")
    public ResponseEntity<ClaimResponse> getClaim(@PathVariable Long id) {

        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    @GetMapping("/policies/{id}/claims")
    public ResponseEntity<List<ClaimResponse>> listByPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimsByPolicy(id));
    }

   @PostMapping("/claims/{id}/review")      // (no body)
    public ResponseEntity<ClaimResponse> reviewClaim(@PathVariable Long id) {
      return ResponseEntity.ok(claimService.reviewClaim(id));
    }

    @PostMapping("/claims/{id}/approve")
    public ResponseEntity<ClaimResponse> approveClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.approveClaim(id));
    }
    @PostMapping("/claims/{id}/reject")
    public ResponseEntity<ClaimResponse> rejectClaim(@PathVariable Long id, @Valid @RequestBody RejectClaimRequest request) {
        return ResponseEntity.ok(claimService.rejectClaim(id, request));
    }
}
