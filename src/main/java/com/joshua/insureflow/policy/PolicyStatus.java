package com.joshua.insureflow.policy;

import jakarta.persistence.Entity;



public enum PolicyStatus {


    PENDING,
    ACTIVE,
    EXPIRED,
    CANCELLED;

    public boolean canTransitionTo(PolicyStatus target) {
        if (target == null || target == this) {
            return false;
        }
        return switch (this) {
            case PENDING -> target == ACTIVE || target == CANCELLED;
            case ACTIVE -> target == EXPIRED || target == CANCELLED;
            case EXPIRED, CANCELLED -> false;
        };
    }

}
