package com.joshua.insureflow.policy;

import com.joshua.insureflow.customer.Customer;
import com.joshua.insureflow.customer.CustomerRepository;
import com.joshua.insureflow.exception.BusinessRuleException;
import com.joshua.insureflow.exception.ResourceNotFoundException;
import com.joshua.insureflow.policy.dto.PolicyRequest;
import com.joshua.insureflow.policy.dto.PolicyResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;

    public PolicyService(PolicyRepository policyRepository,
                         CustomerRepository customerRepository) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
    }

    public PolicyResponse createPolicy(PolicyRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + request.customerId()));

        boolean overlaps = policyRepository.existsOverlappingPolicy(
                request.customerId(),
                request.policyType(),
                List.of(PolicyStatus.PENDING, PolicyStatus.ACTIVE),
                request.startDate(),
                request.expiryDate());

        if (overlaps) {
            throw new BusinessRuleException(
                    "Customer already has an active or pending " + request.policyType()
                            + " policy covering these dates");
        }

        String policyNumber = generatePolicyNumber(request.policyType());

        Policy policy = new Policy(
                policyNumber,
                customer,
                request.policyType(),
                request.startDate(),
                request.expiryDate(),
                request.premium(),
                PolicyStatus.PENDING
        );

        return PolicyResponse.fromEntity(policyRepository.save(policy));
    }

    private String generatePolicyNumber(PolicyType type) {
        String prefix = type.getCode() + "-" + LocalDate.now().getYear() + "-";
        long next = policyRepository.countByPolicyNumberStartingWith(prefix) + 1;
        return prefix + String.format("%03d", next);
    }


    public PolicyResponse activatePolicy(Long policyId) {
      return changeStatus(policyId,PolicyStatus.ACTIVE);
    }

    public PolicyResponse cancelPolicy(Long policyId){
        return changeStatus(policyId,PolicyStatus.CANCELLED);

    }

    private PolicyResponse changeStatus(Long id, PolicyStatus target){

            Policy policy = policyRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + id));

            PolicyStatus current = policy.getStatus();
            if (!current.canTransitionTo(target)) {
                throw new BusinessRuleException(
                        "Cannot change policy from " + current + " to " + target);
            }

            policy.setStatus(target);
            return PolicyResponse.fromEntity(policy);
        }

@Transactional(readOnly = true)
        public PolicyResponse getPolicyById(Long id) {
            Policy policy = policyRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + id));
            return PolicyResponse.fromEntity(policy);
        }

        @Transactional(readOnly = true)
       public List<PolicyResponse> getPoliciesByCustomer(Long customerId) {
            if (!customerRepository.existsById(customerId)) {
                throw new ResourceNotFoundException("Customer not found: " + customerId);
            }
            return policyRepository.findByCustomerId(customerId).stream()
                    .map(PolicyResponse::fromEntity)
                    .toList();
        }

    @Transactional(readOnly = true)
    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(PolicyResponse::fromEntity)
                .toList();
    }

    public int expireOverduePolicies() {
        List<Policy> overdue = policyRepository
                .findByStatusAndExpiryDateBefore(PolicyStatus.ACTIVE, LocalDate.now());

        for (Policy policy : overdue) {
            policy.setStatus(PolicyStatus.EXPIRED);   // dirty checking persists it
        }
        return overdue.size();
    }


}

