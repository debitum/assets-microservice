package com.debitum.assets.port.adapter.investment.persistence;

import com.debitum.assets.domain.model.investment.Invoice;
import com.debitum.assets.domain.model.investment.InvoiceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class EnhancedInvoiceRepository implements InvoiceRepository {
    private final SpringDataInvoiceRepository repository;

    public EnhancedInvoiceRepository(SpringDataInvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Invoice> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Invoice> findAvailableForInvestments() {
        return repository.findAvailableForInvestments();
    }

    @Override
    public List<Invoice> findAll(List<UUID> ids) {
        return repository.findAll(ids);
    }

    @Override
    public Invoice get(UUID invoiceId) {
        return repository.getOne(invoiceId);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }
}
