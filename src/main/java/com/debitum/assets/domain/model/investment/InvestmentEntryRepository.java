package com.debitum.assets.domain.model.investment;


import java.util.List;
import java.util.UUID;

public interface InvestmentEntryRepository {

    List<InvestmentEntry> findAll();

    List<InvestmentEntry> findAll(UUID investmentId);

    InvestmentEntry get(UUID investmentEntryId);

    InvestmentEntry save(InvestmentEntry investmentEntry);

    List<InvestmentEntry> getInvestmentEntriesByInvoiceId(UUID invoiceId);

    List<InvestmentEntry> getInvestmentEntriesByInvoiceIds(List<UUID> invoiceIds);

    List<InvestmentEntry> getInvestmentEntriesUfUser(UUID userId);
}
