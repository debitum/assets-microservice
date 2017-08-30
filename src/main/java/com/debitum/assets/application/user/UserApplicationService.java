package com.debitum.assets.application.user;


import com.debitum.assets.domain.model.user.*;
import com.debitum.assets.domain.model.user.activations.ActionToken;
import com.debitum.assets.domain.model.user.activations.ActionTokenInvalidException;
import com.debitum.assets.domain.model.user.activations.ActionTokenRepository;
import com.debitum.assets.domain.model.user.activations.ExpirationCalculationService;
import com.debitum.assets.port.adapter.user.persistence.UserFilter;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * User application service
 */
@Component
@Transactional
public class UserApplicationService {
    private static Long CLIENT_ROLE_ID = 101L;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final UserNotificationService userNotificationService;
    private final ExpirationCalculationService expirationCalculationService;

    /**
     * Instantiates a new User application service.
     *
     * @param userRepository               the user repository
     * @param userRoleRepository           the user role repository
     * @param actionTokenRepository        the action token repository
     * @param userNotificationService      the user notification service
     * @param expirationCalculationService the expiration calculation service
     */
    UserApplicationService(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           ActionTokenRepository actionTokenRepository,
                           UserNotificationService userNotificationService,
                           ExpirationCalculationService expirationCalculationService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.actionTokenRepository = actionTokenRepository;
        this.userNotificationService = userNotificationService;
        this.expirationCalculationService = expirationCalculationService;
    }

    /**
     * Creates a new user.
     *
     * @param login   the user login name
     * @param phone   the phone number
     * @param company the company name
     * @param name    the user name
     * @return created user entity
     */
    public User createUser(String login,
                           String phone,
                           String company,
                           String name) {
        UserRole role = userRoleRepository.findOne(CLIENT_ROLE_ID);
        User newUser = new User(login, phone, company, name, role);
        return userRepository.save(newUser);
    }


    /**
     * Updates existing user
     *
     * @param userId  the user identifier of updated user
     * @param phone   the phone number
     * @param company the company name
     * @param name    the user name
     * @return created user entity
     */
    public User updateUser(UUID userId,
                           String phone,
                           String company,
                           String name
    ) {
        User user = userRepository.get(userId);
        user.updateUserInfo(phone, company, name);
        return userRepository.save(user);
    }


    /**
     * Updates passwords
     *
     * @param userId      identifier of user
     * @param oldPassword the old user password
     * @param newPassword the new user password
     * @return user with new password set
     */
    public User updatePassword(UUID userId,
                               String oldPassword,
                               String newPassword) {
        Validate.notNull(userId,
                "User identifier is mandatory for password update");
        User user = userRepository.get(userId);
        user.updatePassword(oldPassword, newPassword);
        return userRepository.save(user);
    }

    /**
     * Finds user by given filter
     *
     * @param userFilter filled filter object with filtering params
     * @return list of found users
     */
    public List<User> findAll(UserFilter userFilter) {
        return userRepository.findAll(userFilter);
    }

    /**
     * Initiates password reminding process - created action token for reminder link, sending reminder email
     *
     * @param login of user to start reminding process
     */
    public void initiatePasswordReminder(String login) {
        UserDetails userDetails = userRepository.findUserByLogin(login);
        ActionToken actionToken = actionTokenRepository.save(ActionToken.generatePasswordReminderKey(userDetails.getUser().getId()));
        Instant tokenExpirationDate = expirationCalculationService.calculateExpirationDateForToken(actionToken.getType(), actionToken.getCreatedOn());
        userNotificationService.sendPasswordReminder(login, actionToken.getKey(), tokenExpirationDate);
    }

    /**
     * Changes user status
     *
     * @param userId user identifier
     * @param status that will be set for user
     * @return newly updated user entity
     */
    public User changeUserStatus(UUID userId, UserStatus status) {
        User user = userRepository.get(userId);
        user.changeStatus(status);
        return userRepository.save(user);
    }

    /**
     * Gets user
     *
     * @param userId user identifier
     * @return found user
     */
    public User getUser(UUID userId) {
        return userRepository.get(userId);
    }

    /**
     * Finds user by login
     *
     * @param login of finding user
     * @return mapped user details entity from role and user
     */
    public UserDetails findUserBy(String login) {
        return userRepository.findUserByLogin(login);
    }


    /**
     * Checks if login is used by another user
     *
     * @param login to check for
     * @return true if login is used
     */
    public boolean loginIsUsed(String login) {
        return userRepository.existsByLoginAndNotWithIds(login);
    }

    /**
     * Changes initial password if activation key is correct
     *
     * @param activationKey valid activation key
     * @param password      new password
     */
    public void changeInitialPassword(String activationKey,
                                      String password) {
        Optional<ActionToken> activeInitialPasswordSetupTokenWith =
                actionTokenRepository.getActiveInitialPasswordSetupTokenWith(activationKey);
        ActionToken usedToken = activeInitialPasswordSetupTokenWith
                .orElseThrow(
                        () -> new ActionTokenInvalidException(activationKey)
                ).useKey();
        User user = userRepository.get(usedToken.getUserId()).setInitialPassword(password);
        userRepository.save(user);
        actionTokenRepository.save(usedToken);
    }

    /**
     * Resets password if it was lost by valid activation key which was received to email.
     *
     * @param activationKey valid activation key
     * @param password      new password
     */
    public void resetPassword(String activationKey,
                              String password) {
        Optional<ActionToken> passwordRemindTokenWith =
                actionTokenRepository.getActivePasswordRemindTokenWith(activationKey);
        ActionToken usedToken = passwordRemindTokenWith
                .orElseThrow(
                        () -> new ActionTokenInvalidException(activationKey)
                ).useKey();
        User user = userRepository.get(usedToken.getUserId()).updatePassword(password);
        userRepository.save(user);
    }

    /**
     * Resends activation link to an email
     *
     * @param userId existing user id
     */
    public void resendInitialPasswordActivationLink(UUID userId) {
        User user = userRepository.get(userId);
        user.resendPasswordActivationLink();
    }

}
