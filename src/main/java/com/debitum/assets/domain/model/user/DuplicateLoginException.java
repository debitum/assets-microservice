package com.debitum.assets.domain.model.user;


import com.debitum.assets.domain.model.DomainException;

public class DuplicateLoginException extends DomainException {

    public static final String LOGIN_IS_NOT_UNIQUE = "LOGIN_IS_NOT_UNIQUE";

    public DuplicateLoginException(String login) {
        super(LOGIN_IS_NOT_UNIQUE, "Login {0} already exists", login);
    }
}
