package com.debitum.assets.domain.model.user;


import java.time.Instant;

/**
 * Service which is responsible for delivering notifications about users
 * authentication/authorization data through email.
 */
public interface UserNotificationService {

    /**
     * Send notification that account to DEBITUM was created and initial
     * password set is needed
     *
     * @param login          users login
     * @param actionTokenKey token key that allows to setup new password
     * @param tokenExpirationDate date after which setup new password link will stop working
     * @return notification message detailed info
     */
    UserNotificationDetails notifyAboutInitialPasswordSet(String login, String actionTokenKey, Instant tokenExpirationDate);

    /**
     * @param login          users login for whom password reminder link must be send
     * @param actionTokenKey token key that allows to reset password
     * @param tokenExpirationDate date after which password reset link will stop working
     * @return notification message detailed info
     */
    UserNotificationDetails sendPasswordReminder(String login, String actionTokenKey, Instant tokenExpirationDate);
}
