package com.joshua.insureflow.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PolicyExpiryJob {

    private static final Logger log = LoggerFactory.getLogger(PolicyExpiryJob.class);

    private final PolicyService policyService;

    public PolicyExpiryJob(PolicyService policyService) {
        this.policyService = policyService;
    }

    // Daily at 00:05 (P-8)
    @Scheduled(cron = "0 * * * * *", zone = "Africa/Lagos")
    public void expireOverduePolicies() {
        int count = policyService.expireOverduePolicies();
        log.info("Policy expiry job finished: {} policies set to EXPIRED", count);
    }
}