package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.InvestmentEntry;

public class InvestmentEntryRepaid extends DomainEventPublisher.DomainEvent<InvestmentEntry> {

    public InvestmentEntryRepaid(InvestmentEntry investmentEntry) {
        super(investmentEntry);
    }
}
