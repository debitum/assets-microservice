package com.debitum.assets.resource.dashboard;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DashboardResourceTest extends AuthenticatedIntegrationTestBase {


    @Test
    public void givenUsersInvestments_whenConstructingUsersInvestmentDashboard_thenInvestmentsDividedToPeriods() throws Exception {
        //given existing users investment

        //when
        String content = performAuthenticatedGetRequest(
                CLIENT_1_PRINCIPAL_TOKEN, DashboardResource.ROOT_MAPPING, 200);
        DashboardDTO dashboard = objectMapper.readValue(content, DashboardDTO.class);

        //then
        assertThat(dashboard.getRepayments().getTotalInvestmentsEth()).isGreaterThan(0);
        assertThat(dashboard.getMyInvestments().getTotal()).isGreaterThan(0);
        assertThat(dashboard.getMyInvestments().getAllUserInvestments().getCreatedOnKeys()).hasSize(2);
        assertThat(dashboard.getMyInvestments().getAllUserRepayments().getAmountEthValues()).hasSize(1);
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
