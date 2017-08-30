package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.investment.InvestmentEntry;
import com.debitum.assets.domain.model.event.DomainEventPublisher;

public class InvestmentEntryCreated extends DomainEventPublisher.DomainEvent<InvestmentEntry> {

    public InvestmentEntryCreated(InvestmentEntry investmentEntry) {
        super(investmentEntry);
    }


}
