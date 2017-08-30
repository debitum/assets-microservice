package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;

public class UserAlreadyActivatedException extends DomainException {

    public static final String USER_ALREADY_ACTIVATED = "USER_ALREADY_ACTIVATED";

    public UserAlreadyActivatedException(String login) {
        super(USER_ALREADY_ACTIVATED, "Login {0} already activated", login);
    }
}
