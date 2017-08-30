package com.debitum.assets.port.adapter.investment.persistence;

import com.debitum.assets.domain.model.investment.InvestmentEntry;
import com.debitum.assets.domain.model.investment.InvestmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

interface SpringDataInvestmentEntryRepository extends
        JpaRepository<InvestmentEntry, UUID> {

    List<InvestmentEntry> findAllByInvestmentId(UUID investmentId);

    List<InvestmentEntry> findAllByInvoiceId(UUID invoiceId);

    @Query("SELECT  e FROM InvestmentEntry e INNER JOIN FETCH e.invoice i WHERE e.invoiceId IN ?1")
    List<InvestmentEntry> findAllByInvoiceIds(List<UUID> invoiceIds);

    @Query("SELECT distinct e FROM Investment i INNER JOIN i.investments e WHERE i.userId = ?1")
    List<InvestmentEntry> findAllByUserId(UUID userId);

    @Modifying
    @Query("DELETE from InvestmentEntry a WHERE a.investmentId IN ?1")
    int deleteEntriesOfInvestments(List<UUID>investmentsIds);
}
