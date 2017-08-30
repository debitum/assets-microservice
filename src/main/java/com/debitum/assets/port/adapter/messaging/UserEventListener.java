package com.debitum.assets.port.adapter.messaging;


import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.domain.model.user.UserNotificationService;
import com.debitum.assets.domain.model.user.activations.ActionToken;
import com.debitum.assets.domain.model.user.activations.ActionTokenRepository;
import com.debitum.assets.domain.model.user.activations.ExpirationCalculationService;
import com.debitum.assets.domain.model.user.events.UserCreated;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;

import static com.debitum.assets.AssetsApplication.TEST_PROFILE;
import static com.debitum.assets.domain.model.user.activations.ActionToken.generateInitialPasswordSetupKey;


@Component
@Profile(value = "!" + TEST_PROFILE)
class UserEventListener {

    private final ActionTokenRepository actionTokenRepository;
    private final UserNotificationService userNotificationService;
    private final ExpirationCalculationService expirationCalculationService;

    UserEventListener(ActionTokenRepository actionTokenRepository,
                      UserNotificationService userNotificationService,
                      ExpirationCalculationService expirationCalculationService) {
        this.actionTokenRepository = actionTokenRepository;
        this.userNotificationService = userNotificationService;
        this.expirationCalculationService = expirationCalculationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onEvent(UserCreated event) {
        User user = event.getMessage();
        ActionToken actionToken = actionTokenRepository.save(generateInitialPasswordSetupKey(user.getId()));
        Instant tokenExpirationDate = expirationCalculationService.calculateExpirationDateForToken(actionToken.getType(), actionToken.getCreatedOn());
        userNotificationService.notifyAboutInitialPasswordSet(user.getLogin(), actionToken.getKey(), tokenExpirationDate);
    }
}
