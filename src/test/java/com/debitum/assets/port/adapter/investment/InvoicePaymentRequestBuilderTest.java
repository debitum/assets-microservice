package com.debitum.assets.port.adapter.investment;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.application.investment.InvoiceApplication;
import com.debitum.assets.domain.model.investment.Invoice;
import com.debitum.assets.port.adapter.investment.InvoicePaymentRequestBuilder.ContractInvoicePaymentRequest;
import org.junit.Test;

import javax.inject.Inject;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class InvoicePaymentRequestBuilderTest extends IntegrationTestBase {

    @Inject
    private InvoicePaymentRequestBuilder invoicePaymentRequestBuilder;
    @Inject
    private InvoiceApplication invoiceApplication;

    @Test
    public void givenInvoice_whenInvoicePaid_thenBlockcainPaymentRequestConstructed() {
        //given
        UUID EXISTING_INVOICE_ID = UUID.fromString("761ef8d2-7b8b-11e7-bb31-be2e44b06b34");
        Invoice invoice = invoiceApplication.get(EXISTING_INVOICE_ID);

        //when
        invoiceApplication.repayInvoices(newArrayList(EXISTING_INVOICE_ID));
        ContractInvoicePaymentRequest request = invoicePaymentRequestBuilder.constructInvoicePaymentRequestFor(invoice);

        //then
        assertThat(request).isNotNull();
        assertThat(request.getTokens().toString()).isEqualTo("65f4458e;e3c42b12;68c4244d");
        assertThat(request.getInvoiceEntries().toString()).isEqualTo("00b583c9-8229-4e77-9c5e-684a41ff4fae;14ab4c32-28d2-4887-8210-89566709593a;d19854b3-5f06-463f-bb0c-b753a3fe7e7e");
        assertThat(request.getPays().getValue()).isNotEmpty();
        request.getPays().getValue().stream().forEach(pay ->
                assertThat(pay.getValue().intValueExact()).isGreaterThan(0)
        );
    }
}
