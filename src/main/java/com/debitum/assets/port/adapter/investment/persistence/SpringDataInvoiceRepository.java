package com.debitum.assets.port.adapter.investment.persistence;


import com.debitum.assets.domain.model.investment.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface SpringDataInvoiceRepository extends
        JpaRepository<Invoice, UUID> {

    @Query("SELECT i FROM Invoice  i WHERE i.status <> com.debitum.assets.domain.model.investment.InvoiceStatus.REPAID")
    List<Invoice> findAvailableForInvestments();
}
