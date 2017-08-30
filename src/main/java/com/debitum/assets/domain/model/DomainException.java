package com.debitum.assets.domain.model;


import java.text.MessageFormat;

/**
 * Use this exception when it's specific. I.e. when domain experts tell that this exception is important and should
 * have specific message.
 */
public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    public DomainException(String errorCode,
                           String error,
                           Object... errorArgs) {
        super(MessageFormat.format(
                error,
                errorArgs
        ));
        Preconditions.notBlank(
                errorCode,
                "Can't create domain exception. Please define error code."
        );
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}