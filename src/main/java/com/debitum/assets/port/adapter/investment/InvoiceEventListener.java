package com.debitum.assets.port.adapter.investment;

import com.debitum.assets.AssetsApplication;
import com.debitum.assets.domain.model.investment.InvestmentNotificationService;
import com.debitum.assets.domain.model.investment.Invoice;
import com.debitum.assets.domain.model.investment.events.InvoiceRepaid;
import com.debitum.assets.port.adapter.watcher.contracts.DebtCoverageCollector;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.ExecutionException;

@Component
@Profile(value = "!" + AssetsApplication.TEST_PROFILE)
class InvoiceEventListener {

    private final InvoicePaymentRequestBuilder invoicePaymentRequestBuilder;
    private final DebtCoverageCollector debtCoverageCollector;
    private final InvestmentNotificationService investmentNotificationService;

    public InvoiceEventListener(InvoicePaymentRequestBuilder invoicePaymentRequestBuilder,
                                DebtCoverageCollector debtCoverageCollector,
                                InvestmentNotificationService investmentNotificationService) {
        this.invoicePaymentRequestBuilder = invoicePaymentRequestBuilder;
        this.debtCoverageCollector = debtCoverageCollector;
        this.investmentNotificationService = investmentNotificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onEvent(InvoiceRepaid event) throws ExecutionException, InterruptedException {
        Invoice invoice = event.getMessage();
        InvoicePaymentRequestBuilder.ContractInvoicePaymentRequest request = invoicePaymentRequestBuilder.constructInvoicePaymentRequestFor(invoice);
        debtCoverageCollector.payForInvoices(request.getTokens(), request.getInvoiceEntries(), request.getPays());

        for (int i = 0; i < request.getInvestmentIds().size(); i++) {
            investmentNotificationService.notifyAboutInvestmentRepayInBlockchainContract(
                    request.getInvestmentIds().get(i),
                    request.getInvestmentEntriesIds().get(i)
            );
        }
    }
}
