package com.debitum.assets.domain.model.investment;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.application.investment.InvestmentApplication;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class InvestmentNotificationServiceTest extends IntegrationTestBase {

    @Inject
    private InvestmentNotificationService investmentNotificationService;
    @Inject
    private InvestmentApplication investmentApplication;

    private static final String investment_add_to_blockchain_notification = "src/test/resources/given/investment-add-to-blockchain-notification.html";
    private static final String investment_payment_notification = "src/test/resources/given/investment-payment-notification.html";
    private static final String investment_delete_notification = "src/test/resources/given/investment-delete-notification.html";
    private static final String investment_repayment_notification = "src/test/resources/given/investment-repayment-notification.html";

    @Test
    public void givenExistingInvestment_whenInvestmentAddedToBlockchain_thenUserIsNotified() {
        //given
        UUID EXISTING_INVESTMENT_ID = fromString("65f4458e-d350-4927-b64b-7fe6a909fc6d");
        Investment investment = investmentApplication.get(EXISTING_INVESTMENT_ID);

        //when
        InvestmentNotificationService.InvestmentNotificationDetails investmentNotificationDetails = investmentNotificationService.notifyAboutInvestmentAddToBlockchainContract(investment);

        //then
        assertThat(investmentNotificationDetails.getReceiver()).isEqualTo("client1@debitum.network");
        assertThat(investmentNotificationDetails.getSubject()).isEqualTo("DEBITUM investment added to Ethereum blockchain");
        assertThat(investmentNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(investment_add_to_blockchain_notification)
        ));
    }

    @Test
    public void givenExistingInvestment_whenInvestmentPaid_thenUserIsNotified() {
        //given
        UUID EXISTING_INVESTMENT_ID = fromString("65f4458e-d350-4927-b64b-7fe6a909fc6d");
        Investment investment = investmentApplication.get(EXISTING_INVESTMENT_ID);

        //when
        InvestmentNotificationService.InvestmentNotificationDetails investmentNotificationDetails = investmentNotificationService.notifyAboutInvestmentPaymentInBlockchainContract(investment);

        //then
        assertThat(investmentNotificationDetails.getReceiver()).isEqualTo("client1@debitum.network");
        assertThat(investmentNotificationDetails.getSubject()).isEqualTo("DEBITUM notification about retrieved investment payment in Ethereum blockchain");
        assertThat(investmentNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(investment_payment_notification)
        ));
    }

    @Test
    public void givenExistingInvestment_whenInvestmentDeleted_thenUserIsNotified() {
        //given
        UUID EXISTING_INVESTMENT_ID = fromString("65f4458e-d350-4927-b64b-7fe6a909fc6d");
        Investment investment = investmentApplication.get(EXISTING_INVESTMENT_ID);

        //when
        InvestmentNotificationService.InvestmentNotificationDetails investmentNotificationDetails = investmentNotificationService.notifyAboutInvestmentDelete(investment);

        //then
        assertThat(investmentNotificationDetails.getReceiver()).isEqualTo("client1@debitum.network");
        assertThat(investmentNotificationDetails.getSubject()).isEqualTo("Debitum investment was deleted");
        assertThat(investmentNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(investment_delete_notification)
        ));
    }

    @Test
    public void givenExistingInvestment_whenInvoiceRepaid_thenUserIsNotified() {
        //given
        UUID EXISTING_INVESTMENT_ID = fromString("65f4458e-d350-4927-b64b-7fe6a909fc6d");
        UUID EXISTING_INVESTMENT_ENTRY_ID = fromString("a4551182-98c5-4ad2-a984-c77f3735cedd");

        //when
        InvestmentNotificationService.InvestmentNotificationDetails investmentNotificationDetails = investmentNotificationService.notifyAboutInvestmentRepayInBlockchainContract(EXISTING_INVESTMENT_ID, EXISTING_INVESTMENT_ENTRY_ID);

        //then
        assertThat(investmentNotificationDetails.getReceiver()).isEqualTo("client1@debitum.network");
        assertThat(investmentNotificationDetails.getSubject()).isEqualTo("DEBITUM notification about invoice repayment");
        assertThat(investmentNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(investment_repayment_notification)
        ));
    }
}
