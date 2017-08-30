package com.debitum.assets.domain.model.user;


import com.google.common.collect.Sets;

import java.util.Set;

public enum UserStatus {

    ACTIVE, PENDING, INACTIVE;

    public static Set<UserStatus> availableTransitions(UserStatus startState) {
        switch (startState) {
            case ACTIVE:
                return Sets.newHashSet(INACTIVE);
            case INACTIVE:
                return Sets.newHashSet(ACTIVE);
            default:
                return Sets.newHashSet();
        }
    }
}
