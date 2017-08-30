package com.debitum.assets.domain.model.user.activations;


import com.debitum.assets.domain.model.DomainException;

public class ActionTokenInvalidException extends DomainException {

    public static final String ACTION_KEY_INVALID = "ACTION_KEY_INVALID";

    public ActionTokenInvalidException(String key) {
        super(ACTION_KEY_INVALID, "Action token key {0} invalid", key);
    }
}
