package com.debitum.assets.domain.model.investment.events;


import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.investment.Investment;

public class InvestmentSentToBlockchain extends DomainEventPublisher.DomainEvent<Investment> {

    public InvestmentSentToBlockchain(Investment investment) {
        super(investment);
    }
}
