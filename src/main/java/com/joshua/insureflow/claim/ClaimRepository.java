package com.joshua.insureflow.claim;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByPolicyIdOrderBySubmittedAtDesc(Long policyId);
}