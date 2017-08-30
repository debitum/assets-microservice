package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.Invoice;

public class InvoiceRepaid extends DomainEventPublisher.DomainEvent<Invoice> {

    public InvoiceRepaid(Invoice invoice) {
        super(invoice);
    }
}
