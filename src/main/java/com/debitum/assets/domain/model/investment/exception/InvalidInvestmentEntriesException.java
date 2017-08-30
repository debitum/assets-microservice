package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;

public class InvalidInvestmentEntriesException extends DomainException {

    /**
     * Exception code.
     */
    public static final String INVESTMENT_ENTRIES_INVALID = "INVESTMENT_ENTRIES_INVALID";

    /**
     * Instantiates a new Domain access exception.
     */
    public InvalidInvestmentEntriesException() {
        super(INVESTMENT_ENTRIES_INVALID, "Investment entries has different investment identifiers");
    }
}
