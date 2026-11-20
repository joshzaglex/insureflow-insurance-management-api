package com.joshua.insureflow;

import com.joshua.insureflow.policy.PolicyStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyStatusTest {

    @Test
    void pendingCanBecomeActiveOrCancelled() {
        assertTrue(PolicyStatus.PENDING.canTransitionTo(PolicyStatus.ACTIVE));
        assertTrue(PolicyStatus.PENDING.canTransitionTo(PolicyStatus.CANCELLED));
    }

    @Test
    void pendingCannotExpire() {
        assertFalse(PolicyStatus.PENDING.canTransitionTo(PolicyStatus.EXPIRED));
    }

    @Test
    void activeCanExpireOrCancel() {
        assertTrue(PolicyStatus.ACTIVE.canTransitionTo(PolicyStatus.EXPIRED));
        assertTrue(PolicyStatus.ACTIVE.canTransitionTo(PolicyStatus.CANCELLED));
    }

    @Test
    void activeCannotGoBackToPending() {
        assertFalse(PolicyStatus.ACTIVE.canTransitionTo(PolicyStatus.PENDING));
    }

    @Test
    void sameStatusIsNotATransition() {
        assertFalse(PolicyStatus.ACTIVE.canTransitionTo(PolicyStatus.ACTIVE));
    }

    @Test
    void terminalStatesCannotMove() {
        for (PolicyStatus target : PolicyStatus.values()) {
            assertFalse(PolicyStatus.EXPIRED.canTransitionTo(target));
            assertFalse(PolicyStatus.CANCELLED.canTransitionTo(target));
        }
    }

    @Test
    void nullTargetIsRejected() {
        assertFalse(PolicyStatus.PENDING.canTransitionTo(null));
    }
}