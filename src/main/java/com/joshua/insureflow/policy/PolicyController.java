package com.joshua.insureflow.policy;

import com.joshua.insureflow.policy.dto.PolicyRequest;
import com.joshua.insureflow.policy.dto.PolicyResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PolicyController {

 private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // Create a new policy
    @PostMapping("/api/policies")
    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody
                                                       PolicyRequest request) {
        PolicyResponse created = policyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/policies/" + created.id())
                .body(created);

    }

    @GetMapping("/api/policies")
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/api/policies/{id}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @GetMapping("/api/customers/{id}/policies")
    public ResponseEntity<List<PolicyResponse>> getByCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPoliciesByCustomer(id));
    }

    @PostMapping("/api/policies/{id}/activate")
    public ResponseEntity<PolicyResponse> activatePolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.activatePolicy(id));
    }

    @PostMapping("/api/policies/{id}/cancel")
    public ResponseEntity<PolicyResponse> cancelPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.cancelPolicy(id));
    }
}
