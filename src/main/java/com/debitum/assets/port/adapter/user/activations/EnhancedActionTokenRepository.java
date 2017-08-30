package com.debitum.assets.port.adapter.user.activations;


import com.debitum.assets.domain.model.user.activations.ActionToken;
import com.debitum.assets.domain.model.user.activations.ActionTokenRepository;
import com.debitum.assets.domain.model.user.activations.KeyType;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.debitum.assets.domain.model.user.activations.KeyType.*;


@Component
class EnhancedActionTokenRepository implements ActionTokenRepository {

    private final SpringDataActionTokenRepository repository;

    EnhancedActionTokenRepository(SpringDataActionTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public ActionToken save(ActionToken actionToken) {
        return repository.save(actionToken);
    }

    @Override
    public Optional<ActionToken> getActiveInitialPasswordSetupTokenWith(String key) {
        return repository.findActiveTokenWith(key, INITIAL_PASSWORD_SET);
    }

    @Override
    public Optional<ActionToken> getActivePasswordRemindTokenWith(String key) {
        return repository.findActiveTokenWith(key, PASSWORD_REMIND);
    }

    @Override
    public void markUserLoginTokensAsUsed(Long userId) {
        repository.markUserLoginTokensAsUsed(userId, Instant.now());
    }

    @Override
    public void markUsersTokensAsUsed(UUID userId, KeyType type) {
        repository.markUsersTokensAsUsed(userId, type, Instant.now());
    }

    @Override
    public Optional<ActionToken> getActiveLoginActionTokenCreatedLaterThanGivenTime(Long userId, String key, Instant notOlderThan) {
        return repository.findActiveTokenWith(userId, key, LOGIN, notOlderThan);
    }

    @Override
    @Transactional
    public int deleteTokensCreatedBeforeGivenDate(KeyType type, Instant createdBefore) {
        return repository.deleteExpiredTokens(type, createdBefore);
    }


}
