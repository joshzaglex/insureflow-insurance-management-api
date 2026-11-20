package com.joshua.insureflow.policy;

import com.joshua.insureflow.customer.Customer;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyType policyType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal premium;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Policy() {
        // required by JPA
    }

    public Policy(String policyNumber, Customer customer, PolicyType policyType,
                  LocalDate startDate, LocalDate expiryDate,
                  BigDecimal premium, PolicyStatus status) {
        this.policyNumber = policyNumber;
        this.customer = customer;
        this.policyType = policyType;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.premium = premium;
        this.status = status;
    }



    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getPolicyNumber() { return policyNumber; }
    public Customer getCustomer() { return customer; }
    public PolicyType getPolicyType() { return policyType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public BigDecimal getPremium() { return premium; }
    public PolicyStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(PolicyStatus status) { this.status = status; }
}