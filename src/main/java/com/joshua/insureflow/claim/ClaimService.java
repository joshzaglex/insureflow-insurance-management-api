package com.joshua.insureflow.claim;

import com.joshua.insureflow.claim.dto.ClaimResponse;
import com.joshua.insureflow.claim.dto.CreateClaimRequest;
import com.joshua.insureflow.claim.dto.RejectClaimRequest;
import com.joshua.insureflow.exception.BusinessRuleException;
import com.joshua.insureflow.exception.ResourceNotFoundException;
import com.joshua.insureflow.policy.Policy;
import com.joshua.insureflow.policy.PolicyRepository;
import com.joshua.insureflow.policy.PolicyStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;

@Service
@Transactional
public class ClaimService {
    private final ClaimRepository claimRepository;

    private final PolicyRepository policyRepository;
    private static final ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");

    public ClaimService(ClaimRepository claimRepository, PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    public ClaimResponse submitClaim(Long policyId, CreateClaimRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Policy with ID " + policyId + " does not exist."));


        validateClaimEligibility(policy, request.incidentDate());
        Claim claim = new Claim(
                policy,
                request.claimAmount(),
                request.incidentDate(),
                request.description()
        );

        return ClaimResponse.fromEntity(claimRepository.save(claim));


    }

    private void validateClaimEligibility(Policy policy, LocalDate incidentDate) {


        if (policy.getStatus() != (PolicyStatus.ACTIVE) ) {
            throw new BusinessRuleException("policy not active");
        }

        if (incidentDate.isBefore(policy.getStartDate())) {
            throw new BusinessRuleException("incident date is before policy coverage period");
        }
        if (incidentDate.isAfter(policy.getExpiryDate())) {
            throw new BusinessRuleException("incident date is after policy coverage period");

        }
        if (incidentDate.isAfter(LocalDate.now(LAGOS_ZONE))) {
            throw new BusinessRuleException("Incident date cannot be in the future");
        }


    }

    @Transactional(readOnly = true)
    public ClaimResponse getClaimById(Long claimId) {


        return claimRepository.findById(claimId)
                .map(ClaimResponse::fromEntity)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Claim with ID " + claimId + " does not exist."));

    }


    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsByPolicy(Long policyId) {
        if (!policyRepository.existsById(policyId)) {
            throw new ResourceNotFoundException("Policy with ID " + policyId + " does not exist.");
        }

        return claimRepository.findByPolicyIdOrderBySubmittedAtDesc(policyId)
                .stream()
                .map(ClaimResponse::fromEntity)
                .toList();
    }

    public ClaimResponse reviewClaim(Long claimId) {
        return changeStatus(claimId, ClaimStatus.UNDER_REVIEW, Claim::review);
    }

    public ClaimResponse approveClaim(Long claimId) {
        return changeStatus(claimId, ClaimStatus.APPROVED, Claim::approve);
    }

    public ClaimResponse rejectClaim(Long claimId, RejectClaimRequest request) {
        return changeStatus(claimId, ClaimStatus.REJECTED,
                claim -> claim.reject(request.reason()));
    }

    private ClaimResponse changeStatus(Long claimId, ClaimStatus target, Consumer<Claim> action) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Claim with ID " + claimId + " does not exist."));

        ClaimStatus current = claim.getStatus();
        if (!current.canTransitionTo(target)) {
            throw new BusinessRuleException(
                    "Cannot change claim from " + current + " to " + target);
        }

        action.accept(claim);

        return ClaimResponse.fromEntity(claim);
    }


}
