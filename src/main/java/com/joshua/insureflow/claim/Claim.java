package com.joshua.insureflow.claim;

import com.joshua.insureflow.policy.Policy;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal claimAmount;

    @Column(nullable = false)
    private LocalDate incidentDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @Column()
    private String rejectionReason;

    @Column(nullable = false,updatable = false)
    private LocalDateTime submittedAt;

    @Column
    private LocalDateTime decidedAt;

    protected Claim() {
    }

    public Claim(Policy policy, BigDecimal claimAmount, LocalDate incidentDate, String description) {
        this.policy = policy;
        this.claimAmount = claimAmount;
        this.incidentDate = incidentDate;
        this.description = description;
        this.status = ClaimStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Policy getPolicy() {
        return policy;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public String getDescription() {
        return description;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getDecidedAt() {
        return decidedAt;
    }

    public void approve() {
        this.status = ClaimStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();
    }

    public void review(){
        this.status = ClaimStatus.UNDER_REVIEW;

    }

    public void reject(String reason) {
        this.rejectionReason = reason;
        this.status = ClaimStatus.REJECTED;
        this.decidedAt = LocalDateTime.now();
    }
}
