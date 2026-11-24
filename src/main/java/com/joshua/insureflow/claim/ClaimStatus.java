package com.joshua.insureflow.claim;


public enum ClaimStatus {
    SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED;

    public boolean canTransitionTo(ClaimStatus target) {
        if (target == null || target == this) {
            return false;
        }
        return switch (this) {
            case SUBMITTED -> target == UNDER_REVIEW ;
            case UNDER_REVIEW -> target == APPROVED || target == REJECTED;
            case APPROVED,REJECTED -> false;
        };
    }
}
