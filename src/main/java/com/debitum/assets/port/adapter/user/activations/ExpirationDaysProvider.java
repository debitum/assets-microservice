package com.debitum.assets.port.adapter.user.activations;


import com.debitum.assets.domain.model.user.activations.KeyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
class ExpirationDaysProvider {

    private final Map<KeyType, Integer> expirationDaysProvider = new HashMap<>();
    private final Integer investmentsExpirationAfterDays;

    ExpirationDaysProvider(
            @Value("${user-authorities.actionToken.passwordRemind.expiresAfterDays}") String passwordRemindExpiresAfterDays,
            @Value("${user-authorities.actionToken.initialPasswordSet.expiresAfterDays}") String initialPasswordSetExpiresAfterDays,
            @Value("${investments.expiresAfterDays:1}") String investmentsExpirationAfterDays) {

        expirationDaysProvider.put(KeyType.PASSWORD_REMIND, Integer.valueOf(passwordRemindExpiresAfterDays));
        expirationDaysProvider.put(KeyType.INITIAL_PASSWORD_SET, Integer.valueOf(initialPasswordSetExpiresAfterDays));
        this.investmentsExpirationAfterDays = Integer.valueOf(investmentsExpirationAfterDays);
    }

    Integer getActionTokenExpirationDays(KeyType keyType) {
        return expirationDaysProvider.getOrDefault(keyType, 0);
    }

    Integer getInvestmentExpirationDays(){
        return investmentsExpirationAfterDays;
    }
}
