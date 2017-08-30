package com.debitum.assets.resource.investment;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;

import static com.debitum.assets.domain.model.investment.LoanType.INVOICE_FINANCING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvoiceResourceTest extends AuthenticatedIntegrationTestBase {

    @Test
    public void givenExistingInvoices_whenListingAvailableForInvestment_thenReturnedAll() throws Exception {
        //given existing invoices

        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING, 200);
        InvoiceDTO[] invoices = objectMapper.readValue(contentAsString, InvoiceDTO[].class);

        //then
        assertThat(invoices.length).isEqualTo(3);
        assertThat(invoices[0].getId()).isNotNull();
        assertThat(invoices[0].getLoanType()).isEqualTo(INVOICE_FINANCING);
        assertThat(invoices[0].getInterestRate()).isEqualTo(12.51d);
        assertThat(invoices[0].getAvailableForInvestment()).isEqualTo(2000.0d);
        assertThat(invoices[0].getLoanAmount()).isEqualTo(2000.0d);
        assertThat(invoices[0].getIssueDate()).isNotNull();
        assertThat(invoices[0].getUpdatedOn()).isNotNull();
        assertThat(invoices[0].getCreatedOn()).isNotNull();
        assertThat(invoices[0].getOriginator()).isNotNull();
        assertThat(invoices[0].getLoanBalance()).isNotNull();
        assertThat(invoices[0].getTerm()).isNotNull();
        assertThat(invoices[0].getListDate()).isNotNull();
        assertThat(invoices[0].getBusinessSector()).isNotNull();
        assertThat(invoices[0].getDescription()).isNotNull();
        assertThat(invoices[0].getInvoiceTransaction()).isNotNull();
        assertThat(invoices[0].getCreditRank()).isNotNull();
    }

    @Test
    public void givenExistingInvoices_whenListingAll_thenReturnedAll() throws Exception {
        //given existing invoices

        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING + "/history", 200);
        InvoiceDTO[] invoices = objectMapper.readValue(contentAsString, InvoiceDTO[].class);

        //then
        assertThat(invoices.length).isEqualTo(4);
        assertThat(invoices[0].getId()).isNotNull();
        assertThat(invoices[0].getLoanType()).isEqualTo(INVOICE_FINANCING);
        assertThat(invoices[0].getInterestRate()).isEqualTo(12.51d);
        assertThat(invoices[0].getAvailableForInvestment()).isEqualTo(2000.0d);
        assertThat(invoices[0].getLoanAmount()).isEqualTo(2000.0d);
        assertThat(invoices[0].getIssueDate()).isNotNull();
        assertThat(invoices[0].getUpdatedOn()).isNotNull();
        assertThat(invoices[0].getCreatedOn()).isNotNull();
        assertThat(invoices[0].getOriginator()).isNotNull();
        assertThat(invoices[0].getLoanBalance()).isNotNull();
        assertThat(invoices[0].getTerm()).isNotNull();
        assertThat(invoices[0].getListDate()).isNotNull();
        assertThat(invoices[0].getBusinessSector()).isNotNull();
        assertThat(invoices[0].getDescription()).isNotNull();
        assertThat(invoices[0].getInvoiceTransaction()).isNotNull();
        assertThat(invoices[0].getCreditRank()).isNotNull();
    }

    @Test
    public void givenNewInvoices_whenUploading_thenNewInvoicesSaved() throws Exception {
        //given
        InvoiceImportDTO invoiceImport = InvoiceImportFixture.fixture(3);

        //when
        performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING + "/upload", invoiceImport, 200);

        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING, 200);
        InvoiceDTO[] loaded = objectMapper.readValue(contentAsString, InvoiceDTO[].class);

        //then
        assertThat(loaded.length).isEqualTo(6);
    }

    @Test
    public void givenNewInvoice_whenPosting_thenNewInvoiceSaved() throws Exception {
        //given
        InvoiceDTO newInvoice = InvoiceFixture.fixture();

        //when
        performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING , newInvoice, 200);

        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvoiceResource.ROOT_MAPPING, 200);
        InvoiceDTO[] loaded = objectMapper.readValue(contentAsString, InvoiceDTO[].class);

        //then
        assertThat(loaded.length).isEqualTo(4);

        Arrays.stream(loaded).forEach(invoice -> {
            assertThat(invoice.getId()).isNotNull();
            assertThat(invoice.getLoanType()).isEqualTo(INVOICE_FINANCING);
            assertThat(invoice.getInterestRate()).isNotNull();
            assertThat(invoice.getAvailableForInvestment()).isNotNull();
            assertThat(invoice.getLoanAmount()).isNotNull();
            assertThat(invoice.getIssueDate()).isNotNull();
            assertThat(invoice.getUpdatedOn()).isNotNull();
            assertThat(invoice.getCreatedOn()).isNotNull();
            assertThat(invoice.getOriginator()).isNotNull();
            assertThat(invoice.getLoanBalance()).isNotNull();
            assertThat(invoice.getTerm()).isNotNull();
            assertThat(invoice.getListDate()).isNotNull();
            assertThat(invoice.getBusinessSector()).isNotNull();
            assertThat(invoice.getDescription()).isNotNull();
            assertThat(invoice.getInvoiceTransaction()).isNotNull();
            assertThat(invoice.getCreditRank()).isNotNull();
        });
    }


    @Override
    public String performRequest(MockHttpServletRequestBuilder builder, int expectedStatus) throws Exception {
        return mockMvc
                .perform(builder)
                .andExpect(
                        status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();
    }
}
