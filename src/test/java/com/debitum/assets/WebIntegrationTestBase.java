package com.debitum.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.ClassRule;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public abstract class WebIntegrationTestBase extends IntegrationTestBase {

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(ExternalSourcesStubServer.getDefaultConfig());

    @Inject
    protected ObjectMapper objectMapper;

    @Inject
    protected MockMvc mockMvc;

    protected <T> T performPost(String url, Object requestBody, Class<T> responseClass) throws Exception {
        MockHttpServletResponse rawResponse = mockMvc.perform(
                post(url)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk()).andReturn().getResponse();
        return objectMapper.readValue(rawResponse.getContentAsString(), responseClass);
    }

}