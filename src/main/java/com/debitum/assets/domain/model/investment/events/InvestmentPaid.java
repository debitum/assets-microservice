package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.Investment;

public class InvestmentPaid extends DomainEventPublisher.DomainEvent<Investment> {

    public InvestmentPaid(Investment investment) {
        super(investment);
    }

}
