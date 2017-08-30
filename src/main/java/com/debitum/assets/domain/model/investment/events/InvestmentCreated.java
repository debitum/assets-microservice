package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.Investment;

public class InvestmentCreated extends DomainEventPublisher.DomainEvent<Investment> {
    public InvestmentCreated(Investment investment) {
        super(investment);
    }
}
