package com.debitum.assets.resource.investment;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BatchedInvestmentEntryResourceTest extends AuthenticatedIntegrationTestBase {

    @Test
    public void givenExistingInvestment_whenSendBatchedPostCommand_thenInvestmentEntriesSaved() throws Exception {
        //given
        UUID EXISTING_INVESTMENT_ID = fromString("65f4458e-d350-4927-b64b-7fe6a909fc6d");
        List<InvestmentEntryDTO> entries = InvestmentEntryFixture.fixture(
                EXISTING_INVESTMENT_ID,
                fromString("0dc6ced3-2f9c-440e-9ad5-012bbb49cb5a"),
                fromString("e163b535-ff79-4f20-ad9b-747b5bf3c6cc")
        );

        //when
        performAuthenticatedPostRequest(
                CLIENT_1_PRINCIPAL_TOKEN, BatchedInvestmentEntryResource.ROOT_MAPPING, entries, 200);
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/{id}/entries", 200, EXISTING_INVESTMENT_ID.toString());
        InvestmentEntryDTO[] load = objectMapper.readValue(contentAsString, InvestmentEntryDTO[].class);

        //then
        assertThat(load).hasSize(2);
        for(int i = 0; i < load.length; i++){
            assertThat(load[i].getAmount()).isNotNull();
            assertThat(load[i].getId()).isNotNull();
            assertThat(load[i].getCreatedOn()).isNotNull();
            assertThat(load[i].getUpdatedOn()).isNotNull();
            assertThat(load[i].getInvestmentId()).isNotNull();
            assertThat(load[i].getInvoiceId()).isNotNull();
            assertThat(load[i].getAmountEth()).isGreaterThan(0);

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
