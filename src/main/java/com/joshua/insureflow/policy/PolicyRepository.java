package com.joshua.insureflow.policy;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy,Long>{
    boolean existsByPolicyNumber(String number );



    List<Policy> findByCustomerId(long id);

    List<Policy> findByStatusAndExpiryDateBefore(PolicyStatus status, LocalDate date);
    long countByPolicyNumberStartingWith(String prefix);
    // Overlap: existing starts on/before new expiry AND existing expires on/after new start
    @Query("""
        SELECT COUNT(p) > 0
        FROM Policy p
        WHERE p.customer.id = :customerId
          AND p.policyType = :policyType
          AND p.status IN :statuses
          AND p.startDate <= :expiryDate
          AND p.expiryDate >= :startDate
        """)
    boolean existsOverlappingPolicy(@Param("customerId") Long customerId,
                                    @Param("policyType") PolicyType policyType,
                                    @Param("statuses") List<PolicyStatus> statuses,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("expiryDate") LocalDate expiryDate);

}