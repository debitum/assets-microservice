package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;
import com.debitum.assets.domain.model.user.UserStatus;

public class InvalidUserStatusTransitionException extends DomainException {

    public static final String INVALID_USER_STATUS_TRANSITION = "INVALID_USER_STATUS_TRANSITION";

    public InvalidUserStatusTransitionException(UserStatus oldStatus, UserStatus newStatus) {
        super(INVALID_USER_STATUS_TRANSITION, "User status can not be changed from {0} to {1}, valid states are {2}",
                oldStatus, newStatus, UserStatus.availableTransitions(oldStatus));
    }
}
