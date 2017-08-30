package com.debitum.assets.domain.model.investment;


import java.util.List;
import java.util.UUID;

public interface InvoiceRepository {
    List<Invoice> findAll();

    List<Invoice> findAvailableForInvestments();

    List<Invoice> findAll(List<UUID> ids);

    Invoice get(UUID invoiceId);

    Invoice save(Invoice invoice);
}
