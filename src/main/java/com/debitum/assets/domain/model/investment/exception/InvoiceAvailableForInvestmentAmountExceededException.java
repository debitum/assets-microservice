package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;

import java.util.UUID;

public class InvoiceAvailableForInvestmentAmountExceededException extends DomainException {

    /**
     * Exception code.
     */
    public static final String INVESTMENT_ENTRIES_INVALID = "INVOICE_AVAILABLE_FOR_INVESTMENT_AMOUNT_EXCEEDED";

    /**
     * Instantiates a new Domain access exception.
     */
    public InvoiceAvailableForInvestmentAmountExceededException(UUID id) {
        super(INVESTMENT_ENTRIES_INVALID, "Invoice id=\"" + id.toString() + "\" Available For Investment Amount Exceeded ");
    }

}
