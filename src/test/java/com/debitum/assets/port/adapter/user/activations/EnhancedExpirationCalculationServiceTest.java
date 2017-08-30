package com.debitum.assets.port.adapter.user.activations;


import com.debitum.assets.IntegrationTestBase;
import com.debitum.assets.domain.model.user.activations.KeyType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class EnhancedExpirationCalculationServiceTest extends IntegrationTestBase {

    @Inject
    private EnhancedExpirationCalculationService expirationCalculationService;

    @Value("${user-authorities.actionToken.passwordRemind.expiresAfterDays}")
    private String passwordRemindExpiresAfterDays;

    @Value("${user-authorities.actionToken.initialPasswordSet.expiresAfterDays}")
    private String initialPasswordSetExpiresAfterDays;

    @Test
    public void givenSomeActionTokens_whenTheyGetExpired_thenTheyGetDeleted() {
        //when
        int deleted = expirationCalculationService.deleteExpiredActionTokens();

        //then
        assertThat(deleted).isEqualTo(3);
    }

    @Test
    public void givenPasswordRemindActionToken_whenCallingExpirationDateCalculations_thenRightResultIsReturned() {
        //given
        KeyType keyType = KeyType.PASSWORD_REMIND;
        Instant now = Instant.now();

        //when
        Instant expirationDate = expirationCalculationService.calculateExpirationDateForToken(keyType, now);

        //then
        assertThat(expirationDate).isEqualTo(now.plus(Duration.ofDays(Integer.valueOf(passwordRemindExpiresAfterDays))));
    }

    @Test
    public void givenInitialPasswordSetActionToken_whenCallingExpirationDateCalculations_thenRightResultIsReturned() {
        //given
        KeyType keyType = KeyType.INITIAL_PASSWORD_SET;
        Instant now = Instant.now();

        //when
        Instant expirationDate = expirationCalculationService.calculateExpirationDateForToken(keyType, now);

        //then
        assertThat(expirationDate).isEqualTo(now.plus(Duration.ofDays(Integer.valueOf(initialPasswordSetExpiresAfterDays))));
    }

    @Test
    public void givenExistingNotPaidInvestments_whenTheyGetExpired_thenTheyGetDeleted() {
        //when
        int deleted = expirationCalculationService.deleteExpiredInvestments();

        //then
        assertThat(deleted).isEqualTo(3);
    }

}
