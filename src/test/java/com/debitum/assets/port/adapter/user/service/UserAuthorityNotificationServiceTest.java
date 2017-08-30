package com.debitum.assets.port.adapter.user.service;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.domain.model.user.UserNotificationDetails;
import org.junit.Test;

import javax.inject.Inject;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

public class UserAuthorityNotificationServiceTest extends IntegrationTestBase {
    private static final String users_activation_notification = "src/test/resources/given/users-activation-notification.html";
    private static final String users_password_reset_notification = "src/test/resources/given/users-password-reset-notification.html";

    @Inject
    UserAuthorityNotificationService userAuthorityNotificationService;

    @Test
    public void givenNewlyCreatedUser_whenNotifyingAboutInitialPasswordSet_thenUserReceivesNotificationWithActivationLink() {
        //given
        String EXISTING_ACTION_TOKEN = "password-activation";
        String EXISTING_USERS_LOGIN = "info@debitum.network";
        LocalDateTime time = LocalDateTime.of(2017, 3, 8, 12, 0);

        //when
        UserNotificationDetails userNotificationDetails = userAuthorityNotificationService.notifyAboutInitialPasswordSet(EXISTING_USERS_LOGIN, EXISTING_ACTION_TOKEN, time.toInstant(ZoneOffset.UTC));

        //then
        assertThat(userNotificationDetails.getReceiver()).isEqualTo(EXISTING_USERS_LOGIN);
        assertThat(userNotificationDetails.getSubject()).isEqualTo("Debitum account created");
        assertThat(userNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(users_activation_notification)
        ));
    }

    @Test
    public void givenExistingUser_whenNotifyingAboutPasswordResetActivation_thenUserReceivesNotificationWithPasswordResetActivationLink() {
        //given
        String EXISTING_ACTION_TOKEN = "reset-password";
        String EXISTING_USERS_LOGIN = "info@debitum.network";
        LocalDateTime time = LocalDateTime.of(2017, 3, 8, 12, 0);

        //when
        UserNotificationDetails userNotificationDetails = userAuthorityNotificationService.sendPasswordReminder(EXISTING_USERS_LOGIN, EXISTING_ACTION_TOKEN, time.toInstant(ZoneOffset.UTC));

        //then
        assertThat(userNotificationDetails.getReceiver()).isEqualTo(EXISTING_USERS_LOGIN);
        assertThat(userNotificationDetails.getSubject()).isEqualTo("Your Debitum password reset request");
        assertThat(userNotificationDetails.getMessage()).isEqualToIgnoringWhitespace(contentOf(
                new File(users_password_reset_notification)
        ));
    }
}
