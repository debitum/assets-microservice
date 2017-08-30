package com.debitum.assets.port.adapter.user.activations;



import com.debitum.assets.domain.model.user.activations.ActionToken;
import com.debitum.assets.domain.model.user.activations.KeyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

interface SpringDataActionTokenRepository extends JpaRepository<ActionToken, Long> {

    @Query("SELECT a FROM ActionToken a WHERE a.key = ?1 AND a.type = ?2 AND a.usedOn IS NULL")
    Optional<ActionToken> findActiveTokenWith(String key, KeyType type);

    @Query("SELECT a FROM ActionToken a WHERE a.userId = ?1 AND a.key = ?2 AND a.type = ?3 and " +
            "(a.usedOn IS NULL AND a.createdOn > ?4)")
    Optional<ActionToken> findActiveTokenWith(Long userId, String key, KeyType type, Instant notOlderThan);

    @Modifying
    @Query("UPDATE ActionToken a SET a.usedOn = ?2 WHERE a.usedOn IS NULL AND a.userId = ?1")
    void markUserLoginTokensAsUsed(Long userId, Instant now);

    @Modifying
    @Query("UPDATE ActionToken a SET a.usedOn = ?3 WHERE a.usedOn IS NULL AND a.userId = ?1 AND a.type = ?2")
    void markUsersTokensAsUsed(UUID userId, KeyType type, Instant now);

    @Modifying
    @Query("DELETE from ActionToken a WHERE a.type = ?1 and a.createdOn < ?2")
    int deleteExpiredTokens(KeyType type, Instant createdBefore);
}
