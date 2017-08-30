package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;

public class ActionTokenAlreadyUsedException extends DomainException {

    public static final String ACTION_KEY_ALREADY_USED = "ACTION_KEY_ALREADY_USED";

    public ActionTokenAlreadyUsedException(String key) {
        super(ACTION_KEY_ALREADY_USED, "Action token key {0} was already used", key);
    }
}
