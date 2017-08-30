package com.debitum.assets.resource.settings;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SettingsResourceTest extends AuthenticatedIntegrationTestBase {

    @Test
    public void givenExistingSetting_whenGetting_thenSettingsProvided() throws Exception {
        //given existing settings

        //then
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, SettingsResource.ROOT_MAPPING, 200);
        SettingsDTO settings = objectMapper.readValue(contentAsString, SettingsDTO.class);

        //when
        assertThat(settings.getInvestmentContractAddress()).isEqualTo("0xfd2464908a61b055fb992a6a542c8614d9fea526");
        assertThat(settings.getInvestmentContractAbi()).isEqualTo("[{\"constant\":false,\"inputs\":[],\"name\":\"kill\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"debtCoverageCollectorAddress\",\"type\":\"address\"}],\"name\":\"upgradeDebtCoverageCollector\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"contractToken\",\"type\":\"string\"}],\"name\":\"sendCoin\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":true,\"type\":\"function\"},{\"inputs\":[{\"name\":\"debtCoverageCollectorAddress\",\"type\":\"address\"}],\"payable\":false,\"type\":\"constructor\"},{\"payable\":true,\"type\":\"fallback\"}]");
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
