package com.debitum.assets.port.adapter.investment.persistence;


import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

interface SpringDataInvestmentRepository extends
        JpaRepository<Investment, UUID> {

    List<Investment> findAllByUserId(UUID userId);

    Investment getByContractToken(String contractToken);

    @Modifying
    @Query("DELETE from Investment a WHERE (a.status <> ?1 OR a.status IS NULL) AND a.createdOn < ?2")
    int deleteExpiredTokens(InvestmentStatus status, Instant createdBefore);

    @Query("SELECT a from Investment a WHERE (a.status <> ?1 OR a.status IS NULL) AND a.createdOn < ?2")
    List<Investment> getExpiredInvestmentIds(InvestmentStatus status, Instant createdBefore);
}
