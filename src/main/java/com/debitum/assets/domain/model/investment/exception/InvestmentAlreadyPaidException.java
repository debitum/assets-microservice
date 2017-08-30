package com.debitum.assets.domain.model.investment.exception;

import com.debitum.assets.domain.model.DomainException;

public class InvestmentAlreadyPaidException extends DomainException {

    /**
     * Exception code.
     */
    public static final String INVESTMENT_ALREADY_PAID = "INVESTMENT_ALREADY_PAID";

    /**
     * Instantiates a new Domain access exception.
     */
    public InvestmentAlreadyPaidException() {
        super(INVESTMENT_ALREADY_PAID, "Investment already paid");
    }
}
