package com.debitum.assets.port.adapter.user.activations;


import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.domain.model.user.activations.ActionTokenRepository;
import com.debitum.assets.domain.model.user.activations.ExpirationCalculationService;
import com.debitum.assets.domain.model.user.activations.KeyType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
class EnhancedExpirationCalculationService implements ExpirationCalculationService {

    private final ActionTokenRepository actionTokenRepository;
    private final ExpirationDaysProvider expirationDaysProvider;
    private final InvestmentApplication investmentApplication;


    EnhancedExpirationCalculationService(ActionTokenRepository actionTokenRepository,
                                         ExpirationDaysProvider expirationDaysProvider,
                                         InvestmentApplication investmentApplication) {
        this.actionTokenRepository = actionTokenRepository;
        this.expirationDaysProvider = expirationDaysProvider;
        this.investmentApplication = investmentApplication;
    }

    @Scheduled(cron = "0 0 1 * * *")
    int deleteExpiredActionTokens() {
        int deletedCount = 0;
        deletedCount += deleteExpiredTokens(KeyType.PASSWORD_REMIND);
        deletedCount += deleteExpiredTokens(KeyType.INITIAL_PASSWORD_SET);

        return deletedCount;
    }

    @Scheduled(cron = "0 0 1 * * *")
    int deleteExpiredInvestments() {
        Instant deleteBeforeDate = Instant.now().minus(Duration.ofDays(expirationDaysProvider.getInvestmentExpirationDays()));
        return investmentApplication.deleteNotPaidInvestments(deleteBeforeDate);
    }

    private int deleteExpiredTokens(KeyType keyType) {
        Instant deleteBeforeDate = Instant.now().minus(Duration.ofDays(expirationDaysProvider.getActionTokenExpirationDays(keyType)));
        return actionTokenRepository.deleteTokensCreatedBeforeGivenDate(keyType, deleteBeforeDate);
    }

    @Override
    public Instant calculateExpirationDateForToken(KeyType keyType, Instant createdOn) {
        return createdOn.plus(Duration.ofDays(expirationDaysProvider.getActionTokenExpirationDays(keyType)));
    }
}
