package com.debitum.assets.resource.statistics;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvestmentStatisticsResourceTest extends AuthenticatedIntegrationTestBase {

    @Test
    public void givenExistingInvoice_whenGettingSatistics_thenProvidedAllInvestmentsToInvoice() throws Exception {
        //given
        String EXISTING_INVOICE_ID = "761ef8d2-7b8b-11e7-bb31-be2e44b06b34";


        //when
        String content = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentStatisticsResource.ROOT_MAPPING + "/invoices/" + EXISTING_INVOICE_ID, 200);
        InvestmentStatsDTO[] statistics = objectMapper.readValue(content, InvestmentStatsDTO[].class);

        //then
        assertThat(statistics).hasSize(3);
        for (int i = 0; i < statistics.length; i++) {
            assertThat(statistics[i].getAmount()).isGreaterThan(0);
            assertThat(statistics[i].getInvestor()).isNotEmpty();
        }

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
