package com.debitum.assets.port.adapter.investment;

import com.debitum.assets.AssetsApplication;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentNotificationService;
import com.debitum.assets.domain.model.investment.events.InvestmentCreated;
import com.debitum.assets.domain.model.investment.events.InvestmentPaid;
import com.debitum.assets.domain.model.investment.events.InvestmentSentToBlockchain;
import com.debitum.assets.domain.model.watcher.InvestmentPublishingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Profile(value = "!" + AssetsApplication.TEST_PROFILE)
class InvestmentEventListener {

    private final InvestmentPublishingService investmentPublishingService;
    private final InvestmentNotificationService  investmentNotificationService;

    public InvestmentEventListener(InvestmentPublishingService investmentPublishingService,
                                   InvestmentNotificationService  investmentNotificationService) {
        this.investmentPublishingService = investmentPublishingService;
        this.investmentNotificationService = investmentNotificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onEvent(InvestmentCreated event) throws JsonProcessingException {
        Investment investment = event.getMessage();
        investmentPublishingService.publishInvestment(investment.getId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onEvent(InvestmentSentToBlockchain event) {
        Investment investment = event.getMessage();
        investmentNotificationService.notifyAboutInvestmentAddToBlockchainContract(investment);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onEvent(InvestmentPaid event) {
        Investment investment = event.getMessage();
        investmentNotificationService.notifyAboutInvestmentPaymentInBlockchainContract(investment);
    }
}
