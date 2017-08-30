package com.debitum.assets.resource.user;


import com.debitum.assets.AuthenticatedIntegrationTestBase;
import com.debitum.assets.application.user.UserApplicationService;
import com.debitum.assets.domain.model.user.UserDetails;
import com.debitum.assets.resource.helpers.ErrorDTO;
import com.debitum.assets.resource.helpers.IdUuidDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.debitum.assets.RandomValueHelper.randomString;
import static com.debitum.assets.domain.model.user.DuplicateLoginException.LOGIN_IS_NOT_UNIQUE;
import static com.debitum.assets.domain.model.user.UserStatus.*;
import static com.debitum.assets.domain.model.user.exception.PasswordIncorrectException.INCORRECT_PASSWORD;
import static com.debitum.assets.resource.user.PrivateLoginResource.GET_USER_FAILED_NOT_FOUND;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserResourceTest extends AuthenticatedIntegrationTestBase {

    @Inject
    private UserApplicationService userApplicationService;

    @Test
    public void givenExistingUsers_whenGettingAll_thenUserListLoaded() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING, 200);

        UserDTO[] userDTOs = objectMapper.readValue(contentAsString, UserDTO[].class);

        //then
        assertThat(userDTOs.length).isEqualTo(4);
    }


    @Test
    public void givenNotExistingUsers_whenCreatingUserWithExistingLogin_thenErrorThrown() throws Exception {
        //given
        UserDTO newUser = UserDTOFixture.newUser();
        String EXISTING_LOGIN = "client1@debitum.network";
        newUser.setLogin(EXISTING_LOGIN);
        //when
        String contentAsString = performAuthenticatedPostRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING, newUser, 400);


        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);
        assertThat(error.getErrorCode()).isEqualTo(LOGIN_IS_NOT_UNIQUE);
    }


    @Test
    public void givenExistingUsersWithoutPassword_whenSettingInitialPassword_thenUserPasswordSetAndUserActivated() throws Exception {
        //given
        String EXISTING_PASSWORD_ACTION_TOKEN = "password-activation";
        String EXISTING_USER_IDENTIFIER = "f954f7a8-7bfc-11e7-bb31-be2e44b06b34";
        PasswordDTO newPassword = new PasswordDTO("dummy-password");

        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/{id}", 200, EXISTING_USER_IDENTIFIER);

        UserDTO savedUser = objectMapper.readValue(contentAsString, UserDTO.class);

        performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/password/{activationKey}", newPassword, 200, EXISTING_PASSWORD_ACTION_TOKEN);

        contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/{id}", 200, EXISTING_USER_IDENTIFIER);

        //then
        UserDTO updatedUser = objectMapper.readValue(contentAsString, UserDTO.class);
        assertThat(savedUser.getId().toString()).isEqualTo(EXISTING_USER_IDENTIFIER);
        assertThat(savedUser.getStatus()).isEqualTo(PENDING);
        assertThat(updatedUser.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    public void givenExistingUserWithActivatedPasswordResetAction_whenTryingToSetPassword_thenNewPasswordIsSet() throws Exception {
        //given
        String EXISTING_PASSWORD_ACTION_TOKEN = "reset-password";
        PasswordDTO newPassword = new PasswordDTO("dummy-password");
        String EXISTING_USER_LOGIN = "admin@debitum.network";
        String EXISTING_USER_ID = "f954f046-7bfc-11e7-bb31-be2e44b06b34";

        //when
        performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/password-reset/{activationKey}", newPassword, 200, EXISTING_PASSWORD_ACTION_TOKEN);

        String contentAsString = mockMvc
                .perform(
                        get(
                                PrivateLoginResource.ROOT_MAPPING + "/login/{login}/", EXISTING_USER_LOGIN
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                ).andExpect(
                        status().is(200)
                ).andReturn().getResponse().getContentAsString();

        //then
        UserDetailsDTO updatedUser = objectMapper.readValue(contentAsString, UserDetailsDTO.class);
        assertThat(updatedUser.getId().toString()).isEqualTo(EXISTING_USER_ID);
        assertThat(new BCryptPasswordEncoder().matches("dummy-password", updatedUser.getPassword())).isTrue();

    }

    @Test
    public void givenNotExistingUser_whenTryingGetUserByLogin_thenErrorThrownWithStatus400() throws Exception {
        //given
        String EXISTING_USER_LOGIN = "admin_that_not_exsists@debitum.com";

        //when
        String contentAsString = mockMvc
                .perform(
                        get(
                                PrivateLoginResource.ROOT_MAPPING + "/login/{login}/", EXISTING_USER_LOGIN
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                ).andExpect(
                        status().is(400)
                ).andReturn().getResponse().getContentAsString();

        //then
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);
        assertThat(error.getErrorCode()).isEqualTo(GET_USER_FAILED_NOT_FOUND);
    }

    @Test
    public void givenExistingUserWithActivatedPasswordResetAction_whenTryingDeactivate_thenSuccessfulyDeactivated() throws Exception {
        //given
        String EXISTING_USER_LOGIN = "admin@debitum.network";
        UserDTO existingUser = UserDTOFixture.userWith(fromString("f954f046-7bfc-11e7-bb31-be2e44b06b34"));
        existingUser.setStatus(INACTIVE);
        String EXISTING_USER_ID = "f954f046-7bfc-11e7-bb31-be2e44b06b34";

        //when
        performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/{userId}/state", existingUser, 200, EXISTING_USER_ID.toString());

        String contentAsString = mockMvc
                .perform(
                        get(
                                PrivateLoginResource.ROOT_MAPPING + "/login/{login}/", EXISTING_USER_LOGIN
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                ).andExpect(
                        status().is(200)
                ).andReturn().getResponse().getContentAsString();

        //then
        UserDetailsDTO updatedUser = objectMapper.readValue(contentAsString, UserDetailsDTO.class);
        assertThat(updatedUser.getStatus()).isEqualTo(INACTIVE);

    }

    @Test
    public void givenExistingUserWithoutPassword_whenResendingActivationLink_thenMessageWithActivationLinkIsResented() throws Exception {
        //given
        UserDTO newUser = UserDTOFixture.newUser();

        //when
        String contentAsString = performAuthenticatedPostRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING, newUser, 200);

        flush();
        UUID newUserId = objectMapper.readValue(contentAsString, IdUuidDTO.class).getId();

        performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/password-resend/{activationKey}", 200, newUserId.toString());
    }

    @Test
    public void givenExistingUserWithPassword_whenResendingActivationLink_thenError() throws Exception {
        String contentAsString = performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/password-resend/{activationKey}", 400, "f954f046-7bfc-11e7-bb31-be2e44b06b34");

        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);

        assertThat(error.getErrorCode()).isEqualTo("USER_ALREADY_ACTIVATED");
    }

    @Test
    public void givenExistingUserWithPassword_whenResettingPasswordSecondTime_thenError() throws Exception {
        //given
        String EXISTING_PASSWORD_ACTION_TOKEN = "reset-password";

        //when
        resetPasswordWithToken(EXISTING_PASSWORD_ACTION_TOKEN, 200);
        String contentAsString = resetPasswordWithToken(EXISTING_PASSWORD_ACTION_TOKEN, 400);

        //then
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);
        assertThat(error.getErrorCode()).isEqualTo("ACTION_KEY_ALREADY_USED");
    }

    private String resetPasswordWithToken(String passwordActionToken, int expectedStatus) throws Exception {
        PasswordDTO newPassword = new PasswordDTO("dummy-password");

        return mockMvc
                .perform(
                        patch(
                                UserResource.ROOT_MAPPING + "/password-reset/{activationKey}", passwordActionToken
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                                .content(objectMapper.writeValueAsString(newPassword)
                                )
                ).andExpect(
                        status().is(expectedStatus)
                ).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void givenExistingUser_whenResettingToPasswordThatDoesNotMeetRequirements_thenError() throws Exception {
        //given
        String EXISTING_PASSWORD_ACTION_TOKEN = "reset-password";

        //when
        PasswordDTO newPassword = new PasswordDTO("7symbol");

        String contentAsString = mockMvc
                .perform(
                        patch(
                                UserResource.ROOT_MAPPING + "/password-reset/{activationKey}", EXISTING_PASSWORD_ACTION_TOKEN
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON_UTF8_VALUE
                                )
                                .content(objectMapper.writeValueAsString(newPassword)
                                )
                ).andExpect(
                        status().is(500)
                ).andReturn().getResponse().getContentAsString();


        //then
        assertThat(contentAsString.contains("Password should be at least 8 symbols long. Please try again.")).isTrue();

    }


    @Test
    public void givenUserWithInvalidTokenKey_whenResettingPassword_thenError() throws Exception {
        //given
        String INVALID_PASSWORD_ACTION_TOKEN = "invalid-token";

        //when
        String contentAsString = resetPasswordWithToken(INVALID_PASSWORD_ACTION_TOKEN, 400);

        //then
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);
        assertThat(error.getErrorCode()).isEqualTo("ACTION_KEY_INVALID");
    }

    @Test
    public void givenExistingUser_whenPostingNewUserWithExistingLogin_thenErrorThrown() throws Exception {
        //given
        String EXISTING_LOGIN = "client1@debitum.network";
        UserDTO newUser = UserDTOFixture.newUser();
        newUser.setLogin(EXISTING_LOGIN);

        //when
        String contentAsString = performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING, newUser, 400);
        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);

        //then
        assertThat(error.getErrorCode()).isEqualTo(LOGIN_IS_NOT_UNIQUE);
    }

    @Test
    public void givenExistingConcept_whenSearchingIFConceptNameIsUsed_thenReturnsIfConceptNameIsAlreadyUsed() throws Exception {
        //given
        String EXISTING_NAME = "admin@debitum.network";
        String NOT_EXISTING_NAME = "very-new-login@debitum.com";

        //when
        UserLoginDTO existingLogin = performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN,
                UserResource.ROOT_MAPPING + "/logins",
                new UserLoginDTO(EXISTING_NAME),
                UserLoginDTO.class
        );
        UserLoginDTO notExistingLogin = performAuthenticatedPostRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN,
                UserResource.ROOT_MAPPING + "/logins",
                new UserLoginDTO(NOT_EXISTING_NAME),
                UserLoginDTO.class
        );

        //then
        assertThat(existingLogin.getLogin()).isEqualTo(EXISTING_NAME);
        assertThat(existingLogin.isExists()).isTrue();
        assertThat(notExistingLogin.getLogin()).isEqualTo(NOT_EXISTING_NAME);
        assertThat(notExistingLogin.isExists()).isFalse();
    }


    @Test
    public void givenExistingUser_whenUpdatingUsersProfile_thenUsersDataUpdated() throws Exception {
        //given
        UserDTO existingUser = new UserDTO();
        String EXISTING_USER_ID = "8180d9a4-7bfc-11e7-bb31-be2e44b06b34";
        String NEW_USER_NAME = randomString();
        String NEW_USER_PHONE = randomString();
        String NEW_USER_COMPANY = randomString();
        existingUser.setName(NEW_USER_NAME);
        existingUser.setPhone(NEW_USER_PHONE);
        existingUser.setCompany(NEW_USER_COMPANY);


        //when
        performAuthenticatedPutRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/profile", existingUser);
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/{id}", 200, EXISTING_USER_ID);
        UserDTO user = objectMapper.readValue(contentAsString, UserDTO.class);

        //then
        assertThat(user.getName()).isEqualTo(NEW_USER_NAME);
        assertThat(user.getPhone()).isEqualTo(NEW_USER_PHONE);
        assertThat(user.getCompany()).isEqualTo(NEW_USER_COMPANY);
    }

    @Test
    public void givenExistingUser_whenGettingUsersProfile_thenUsersDataReturned() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/profile", 200);

        UserDTO user = objectMapper.readValue(contentAsString, UserDTO.class);

        //then
        assertThat(user.getName()).isEqualTo("Justas Saltinis");
        assertThat(user.getPhone()).isEqualTo("+37064018006");
        assertThat(user.getCompany()).isEqualTo("Debifo");
    }

    @Test
    public void givenExistingUser_whenUpdatingPasswordWithProvidingInvalidOldPassword_thenErrorIsThrown() throws Exception {
        //given
        PasswordChangeDTO passwordChange = new PasswordChangeDTO("invalid-password", "new - password");

        //when
        String contentAsString = performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/profile/password", passwordChange, 400);

        ErrorDTO error = objectMapper.readValue(contentAsString, ErrorDTO.class);

        //then
        assertThat(error.getErrorCode()).isEqualTo(INCORRECT_PASSWORD);
    }

    @Test
    public void givenExistingUser_whenUpdatingPassword_thenPasswordChangedToNewOne() throws Exception {
        //given
        String NEW_PASSWORD = "new - password";
        String OLD_PASSWORD = "SomeOldPsw";
        String LOGIN = "info@debifo.lt";
        PasswordChangeDTO passwordChange = new PasswordChangeDTO(OLD_PASSWORD, NEW_PASSWORD);

        //when
        performAuthenticatedPatchRequest(FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING + "/profile/password", passwordChange, 200);

        //then
        UserDetails userDetails = userApplicationService.findUserBy(LOGIN);
        assertThat(new BCryptPasswordEncoder().matches(NEW_PASSWORD, userDetails.getUser().getPassword())).isTrue();
    }


    @Test
    public void givenExistingUsers_whenGettingAllWithoutFilters_thenAllUsersAreReturned() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING, 200);
        List<UserDTO> users = objectMapper.readValue(contentAsString, new TypeReference<List<UserDTO>>() {
        });

        //then
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void givenExistingUsers_whenGettingAllWithAllStateFilters_thenAllUsersAreReturned() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING +
                        "?states=ACTIVE&states=PENDING&states=INACTIVE", 200);
        List<UserDTO> users = objectMapper.readValue(contentAsString, new TypeReference<List<UserDTO>>() {
        });

        //then
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    public void givenExistingUsers_whenGettingAllWithSomeStateFilters_thenAppropriateUsersAreReturned() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING +
                        "?states=PENDING&states=INACTIVE", 200);
        List<UserDTO> users = objectMapper.readValue(contentAsString, new TypeReference<List<UserDTO>>() {
        });

        //then
        assertThat(users).extracting("status").containsExactly(PENDING, PENDING);
    }

    @Test
    public void givenExistingUsers_whenGettingAllWithSomeStateAndNameFilters_thenAppropriateUsersAreReturned() throws Exception {
        //when
        String contentAsString = performAuthenticatedGetRequest(
                FULL_ACCESS_USER_PRINCIPAL_TOKEN, UserResource.ROOT_MAPPING +
                        "?states=ACTIVE&states=INACTIVE&name=s", 200);
        List<UserDTO> users = objectMapper.readValue(contentAsString, new TypeReference<List<UserDTO>>() {
        });

        //then
        assertThat(users).extracting("id").containsExactly(fromString("f954f046-7bfc-11e7-bb31-be2e44b06b34"), fromString("8180d9a4-7bfc-11e7-bb31-be2e44b06b34"));
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

