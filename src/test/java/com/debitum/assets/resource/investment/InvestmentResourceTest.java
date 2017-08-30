package com.debitum.assets.resource.investment;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import com.debitum.assets.resource.helpers.ErrorDTO;
import com.debitum.assets.resource.helpers.IdUuidDTO;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.stream.Stream;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InvestmentResourceTest extends AuthenticatedIntegrationTestBase {

    @Test
    public void givenNewInvestment_whenPosting_thenInvestmentSaved() throws Exception {
        //given
        InvestmentDTO investmentDTO = InvestmentFixture.fixture(fromString("8180d9a4-7bfc-11e7-bb31-be2e44b06b34"));

        //when
        IdUuidDTO idDTO = performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING, investmentDTO, IdUuidDTO.class);

        //then
        assertThat(idDTO.getId()).isNotNull();
    }

    @Test
    public void givenExistingInvestments_whenGetting_thenReturnedAllUsersInvestments() throws Exception {
        //given existing investments

        //when
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/bucket", 200);
        InvestmentDTO[] loaded = objectMapper.readValue(contentAsString, InvestmentDTO[].class);

        //then
        assertThat(loaded.length).isEqualTo(2);
    }

    @Test
    public void givenExistingInvestments_whenGettingById_thenReturnedUsersInvestmentBySpecifiedId() throws Exception {
        //given
        String EXISTING_INVESTMENT_ID = "e3c42b12-a6b4-4e59-8452-a4598a01bc47";

        //when
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/{id}", 200, EXISTING_INVESTMENT_ID);
        InvestmentDTO loaded = objectMapper.readValue(contentAsString, InvestmentDTO.class);

        //then
        assertThat(loaded).isNotNull();
        assertThat(loaded.getTotalAmount()).isEqualTo(90D);
        assertThat(loaded.getUserFullName()).isEqualTo("Client 1");
    }

    @Test
    public void givenExistingInvestments_whenTryingToGetOtherUsersInvestment_thenErrorThrown() throws Exception {
        //given
        String EXISTING_CLIENTS_2_INVESTMENT_ID = "68c4244d-5c57-490e-98f2-07761b994d7b";

        //when
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/{id}", 400, EXISTING_CLIENTS_2_INVESTMENT_ID);
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);

        //then
        assertThat(error).isNotNull();
        assertThat(error.getErrorCode()).isEqualTo("DOMAIN_ACCESS_RESTRICTION");
        assertThat(error.getErrorMsg()).isEqualTo("User cannot access domain resource");
    }

    @Test
    public void givenExistingInvestments_whenGettingEntries_thenInvestmentEntriesReturned() throws Exception {
        //given
        String EXISTING_INVESTMENT_ID = "65f4458e-d350-4927-b64b-7fe6a909fc6d";

        //when
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/{id}/entries", 200, EXISTING_INVESTMENT_ID);
        InvestmentEntryDTO[] load = objectMapper.readValue(contentAsString, InvestmentEntryDTO[].class);

        //then
        assertThat(load).hasSize(2);
    }

    @Test
    public void givenExistingInvestments_whenTryingToGetOtherUsersInvestmentEntries_thenErrorThrown() throws Exception {
        //given
        String EXISTING_INVESTMENT_ID = "68c4244d-5c57-490e-98f2-07761b994d7b";

        //when
        String contentAsString = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, InvestmentResource.ROOT_MAPPING + "/{id}/entries", 400, EXISTING_INVESTMENT_ID);
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);

        //then
        assertThat(error).isNotNull();
        assertThat(error.getErrorCode()).isEqualTo("DOMAIN_ACCESS_RESTRICTION");
        assertThat(error.getErrorMsg()).isEqualTo("User cannot access domain resource");
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
