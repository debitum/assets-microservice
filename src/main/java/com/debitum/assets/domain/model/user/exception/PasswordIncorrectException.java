package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;

public class PasswordIncorrectException extends DomainException {

    public static final String INCORRECT_PASSWORD = "INCORRECT_PASSWORD";

    public PasswordIncorrectException() {
        super(INCORRECT_PASSWORD, "Users password is incorrect");
    }

}
