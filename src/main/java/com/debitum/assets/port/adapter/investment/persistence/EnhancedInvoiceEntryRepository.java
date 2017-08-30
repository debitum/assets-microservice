package com.debitum.assets.port.adapter.investment.persistence;

import com.debitum.assets.domain.model.investment.InvestmentEntry;
import com.debitum.assets.domain.model.investment.InvestmentEntryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class EnhancedInvoiceEntryRepository implements InvestmentEntryRepository {
    private final SpringDataInvestmentEntryRepository repository;

    public EnhancedInvoiceEntryRepository(SpringDataInvestmentEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InvestmentEntry> findAll() {
        return repository.findAll();
    }

    @Override
    public List<InvestmentEntry> findAll(UUID investmentId) {
        return repository.findAllByInvestmentId(investmentId);
    }

    @Override
    public InvestmentEntry get(UUID investmentEntryId) {
        return repository.getOne(investmentEntryId);
    }

    @Override
    public InvestmentEntry save(InvestmentEntry investmentEntry) {
        return repository.save(investmentEntry);
    }

    @Override
    public List<InvestmentEntry> getInvestmentEntriesByInvoiceId(UUID invoiceId) {
        return repository.findAllByInvoiceId(invoiceId);
    }

    @Override
    public List<InvestmentEntry> getInvestmentEntriesByInvoiceIds(List<UUID> invoiceIds) {
        return repository.findAllByInvoiceIds(invoiceIds);
    }

    @Override
    public List<InvestmentEntry> getInvestmentEntriesUfUser(UUID userId) {
        return repository.findAllByUserId(userId);
    }
}
