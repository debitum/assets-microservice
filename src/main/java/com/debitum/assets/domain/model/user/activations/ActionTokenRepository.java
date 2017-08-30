package com.debitum.assets.domain.model.user.activations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ActionTokenRepository {

    /**
     * Saves or creates action token
     *
     * @param actionToken to save
     * @return saved action token
     */
    ActionToken save(ActionToken actionToken);

    /**
     * Finds active action token for initial password setup
     *
     * @param key of action token
     * @return actions token if exists or is valid
     */
    Optional<ActionToken> getActiveInitialPasswordSetupTokenWith(String key);

    /**
     * Finds active action token for password reminder
     *
     * @param key of action token
     * @return actions token if exists or is valid
     */
    Optional<ActionToken> getActivePasswordRemindTokenWith(String key);

    /**
     * Invalidates all login action tokens for user.
     *
     * @param userId of existing user
     */
    void markUserLoginTokensAsUsed(Long userId);

    /**
     * Invalidates every action token of any type for user.
     *
     * @param userId of existing user
     */
    void markUsersTokensAsUsed(UUID userId, KeyType type);

    /**
     * Gets active login action token created later that given time
     *
     * @param userId of existing user
     * @param key of existing action token
     * @param notOlderThan date till valid
     * @return active action token
     */
    Optional<ActionToken> getActiveLoginActionTokenCreatedLaterThanGivenTime(Long userId, String key, Instant notOlderThan);

    /**
     * Deletes tokens created before given date
     *
     * @param type of action token to delete
     * @param createdBefore of created tokens before
     * @return count of deleted actions tokens
     */
    int deleteTokensCreatedBeforeGivenDate(KeyType type, Instant createdBefore);
}
