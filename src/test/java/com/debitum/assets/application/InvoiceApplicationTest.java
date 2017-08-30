package com.debitum.assets.application;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.application.investment.InvoiceApplication;
import com.debitum.assets.domain.model.investment.InvoiceRepaymentValidation;
import org.assertj.core.util.Lists;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.debitum.assets.domain.model.investment.RepaymentValidationStatus.BALANCE_NOT_ENOUGH;
import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceApplicationTest extends IntegrationTestBase {

    @Inject
    private InvoiceApplication invoiceApplication;


    @Test
    public void givenExistingInvoices_whenValidatingRepayment_thenValidationInfoProvided() {
        //given
        List<UUID> invoiceIds = Lists.newArrayList(UUID.fromString("761ef8d2-7b8b-11e7-bb31-be2e44b06b34"));

        //when
        InvoiceRepaymentValidation invoiceRepaymentValidation = invoiceApplication.invoiceRepaymentValidation(0d, invoiceIds);

        //then
        assertThat(invoiceRepaymentValidation.getBalanceEth()).isEqualTo(0d);
        assertThat(invoiceRepaymentValidation.getInvoiceRepaymentNeededEth()).isGreaterThan(0);
        assertThat(invoiceRepaymentValidation.getStatus()).isEqualTo(BALANCE_NOT_ENOUGH);

    }
}
