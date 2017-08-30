package com.debitum.assets.resource.user;


import com.debitum.assets.WebIntegrationTestBase;
import com.debitum.assets.domain.model.user.UserStatus;
import org.junit.Test;
import org.springframework.http.MediaType;

import static com.debitum.assets.domain.model.user.Authority.ROLE_AUTHENTICATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrivateLoginResourceTest extends WebIntegrationTestBase {

    @Test
    public void givenExistingUser_whenGettingByLogin_thenUserDetailsReturned() throws Exception {
        //given
        String EXISTING_LOGIN = "admin@debitum.network";
        String EXISTING_USERS_PASSWORD = "tricky-password";
        String EXISTING_USERS_PHONE = "+370999";

        //when
        String contentAsString = mockMvc
                .perform(
                        get(
                                PrivateLoginResource.ROOT_MAPPING + "/login/{login}/", EXISTING_LOGIN
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                ).andExpect(
                        status().isOk()
                ).andReturn().getResponse().getContentAsString();

        UserDetailsDTO userDetails = objectMapper.readValue(contentAsString, UserDetailsDTO.class);

        //then
        assertThat(userDetails.getLogin()).isEqualTo(EXISTING_LOGIN);
        assertThat(userDetails.getPhone()).isEqualTo(EXISTING_USERS_PHONE);
        assertThat(userDetails.getPassword()).isEqualTo(EXISTING_USERS_PASSWORD);
        assertThat(userDetails.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userDetails.getAuthorities().size()).isEqualTo(8);
        assertThat(userDetails.getAuthorities()).contains(ROLE_AUTHENTICATED);
    }
}
