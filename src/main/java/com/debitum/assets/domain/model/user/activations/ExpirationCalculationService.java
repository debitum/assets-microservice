package com.debitum.assets.domain.model.user.activations;

import java.time.Instant;

public interface ExpirationCalculationService {

    /**
     * Calculates action tokens validity time after creation by token type.
     *
     * @param keyType different types of tokens
     * @param createdOn date of token creation
     * @return validity time
     */
    Instant calculateExpirationDateForToken(KeyType keyType, Instant createdOn);
}
