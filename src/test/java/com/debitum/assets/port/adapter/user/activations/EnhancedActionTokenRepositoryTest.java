package com.debitum.assets.port.adapter.user.activations;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.domain.model.user.activations.ActionTokenRepository;
import com.debitum.assets.domain.model.user.activations.KeyType;
import org.junit.Test;

import javax.inject.Inject;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EnhancedActionTokenRepositoryTest extends IntegrationTestBase {

    @Inject
    private ActionTokenRepository actionTokenRepository;

    @Test
    public void givenExistingInitialPasswordSetToken_whenActionTokenIsUsed_thenItCannotBeReused() {
        assertThat(actionTokenRepository.getActiveInitialPasswordSetupTokenWith("password-activation").isPresent()).isTrue();
        actionTokenRepository.markUsersTokensAsUsed(UUID.fromString("f954f7a8-7bfc-11e7-bb31-be2e44b06b34"), KeyType.INITIAL_PASSWORD_SET);
        assertThat(actionTokenRepository.getActiveInitialPasswordSetupTokenWith("password-activation").isPresent()).isFalse();
    }
}
