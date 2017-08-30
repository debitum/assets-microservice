package com.debitum.assets;



import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Helper provides realistic scenario testing of authentication.
 */
public abstract class AuthenticatedIntegrationTestBase extends WebIntegrationTestBase {

    public static final String FULL_ACCESS_USER_PRINCIPAL_TOKEN = "85637b01-1564-4e21-b854-fe5ec25cc4e0";
    public static final String CLIENT_1_PRINCIPAL_TOKEN = "3dd7ef15-12d1-4c1e-a9c2-757880ebfb8e";

    public static final String INVALID_ACCESS_USER_PRINCIPAL_TOKEN = "85637b01-1564-4e21-b854-fe5ec25cc4eb";

    private static final String FULL_ACCESS_USER_PRINCIPAL = "full_access";
    private static final String INVALID_ACCESS_USER_PRINCIPAL = "invalid_access";
    private static final String CLIENT_1_ACCESS_USER_PRINCIPAL = "client_access";

    private static final Map<String, String> tokenUserMap = new HashMap<String, String>() {{
        put(FULL_ACCESS_USER_PRINCIPAL_TOKEN, FULL_ACCESS_USER_PRINCIPAL);
        put(INVALID_ACCESS_USER_PRINCIPAL_TOKEN, INVALID_ACCESS_USER_PRINCIPAL);
        put(CLIENT_1_PRINCIPAL_TOKEN, CLIENT_1_ACCESS_USER_PRINCIPAL);
    }};

    public static void stubUserInfoServer() {
        tokenUserMap.forEach((token, principal) -> {
            try {
                stubUserInfoServer(token);
            } catch (IOException e) {
                throw new RuntimeException("Can not find file " + principal + ".json");
            }
        });
    }

    private static void stubUserInfoServer(String token) throws IOException {
        Resource resource = new ClassPathResource("given/principals/" + tokenUserMap.get(token) + ".json");

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        stubFor(com.github.tomakehurst.wiremock.client.WireMock
                .any(urlPathEqualTo("/user"))
                .withHeader("Authorization", equalTo("Bearer " + token))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withStatus(200)
                        .withBody(responseStrBuilder.toString())));
    }


    public String performAuthenticatedGetRequest(String token, String url, int expectedStatus, long pathParamValue) throws Exception {

        MockHttpServletRequestBuilder builder =
                get(url, pathParamValue)
                        .contentType(
                                MediaType.APPLICATION_JSON_UTF8_VALUE
                        );

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedGetRequest(String token, String url, int expectedStatus, String pathParamValue) throws Exception {

        MockHttpServletRequestBuilder builder =
                get(url, pathParamValue)
                        .contentType(
                                MediaType.APPLICATION_JSON_UTF8_VALUE
                        );

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedGetRequest(String token, String url, int expectedStatus) throws Exception {

        MockHttpServletRequestBuilder builder =
                get(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedPostRequest(String token, String url, Object contentObject, int expectedStatus, long... pathParamValues) throws Exception {

        MockHttpServletRequestBuilder builder = post(url, pathParamValues)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(contentObject));

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public <T> T performAuthenticatedPostRequest(String token, String url, Object requestBody, Class<T> responseClass) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8);
        stubAuthorizationIfNeeded(builder, token);
        MockHttpServletResponse rawResponse = mockMvc.perform(
                builder
        ).andExpect(status().isOk()).andReturn().getResponse();


        return objectMapper.readValue(rawResponse.getContentAsString(), responseClass);
    }


    public String performAuthenticatedPatchRequest(String token, String url, Object contentObject, int expectedStatus) throws Exception {

        MockHttpServletRequestBuilder builder = patch(
                url
        )
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(contentObject));

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedPatchRequest(String token, String url, Object contentObject, int expectedStatus, String pathParamValue) throws Exception {

        MockHttpServletRequestBuilder builder = patch(
                url, pathParamValue
        )
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(contentObject));

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedPatchRequest(String token, String url, int expectedStatus, String pathParamValue) throws Exception {

        MockHttpServletRequestBuilder builder =
                patch(url, pathParamValue)
                        .contentType(
                                MediaType.APPLICATION_JSON_UTF8_VALUE
                        );

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedPatchRequest(String token, String url, int expectedStatus, Long pathParamValue) throws Exception {
        MockHttpServletRequestBuilder builder = patch(
                url, pathParamValue
        )
                .contentType(
                        MediaType.APPLICATION_JSON_UTF8_VALUE
                );

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    public String performAuthenticatedPutRequest(String token, String url, Object contentObject) throws Exception {
        return performAuthenticatedPutRequest(token, url, contentObject, 200);
    }

    public String performAuthenticatedPutRequest(String token, String url, Object contentObject, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = put(
                url
        )
                .contentType(
                        MediaType.APPLICATION_JSON_UTF8_VALUE
                )
                .content(
                        objectMapper.writeValueAsString(contentObject)
                );

        stubAuthorizationIfNeeded(builder, token);
        return performRequest(builder, expectedStatus);
    }

    private void stubAuthorizationIfNeeded(MockHttpServletRequestBuilder builder, String token) {
        if (token != null) {
            stubUserInfoServer();
            builder.header("Authorization", "Bearer " + token);
        }
    }

    public abstract String performRequest(MockHttpServletRequestBuilder builder, int expectedStatus) throws Exception;
}
