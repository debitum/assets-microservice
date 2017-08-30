package com.debitum.assets.domain.model.security.exception;


import com.debitum.assets.domain.model.DomainException;

/**
 * Domain access exception which is thrown when users tries to access domain resource which cannot be provided for them.
 */
public class DomainAccessException extends DomainException {

    /**
     * Exception code.
     */
    public static final String DOMAIN_ACCESS_RESTRICTION = "DOMAIN_ACCESS_RESTRICTION";

    /**
     * Instantiates a new Domain access exception.
     */
    public DomainAccessException() {
        super(DOMAIN_ACCESS_RESTRICTION, "User cannot access domain resource");
    }
}
