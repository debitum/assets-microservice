package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;
import com.debitum.assets.domain.model.investment.Invoice;

import java.util.UUID;

public class InvoiceAlreadyRepaidException extends DomainException {

    /**
     * Exception code.
     */
    public static final String INVOICE_ALREADY_REPAID = "INVOICE_ALREADY_REPAID";

    /**
     * Instantiates a new Domain access exception.
     */
    public InvoiceAlreadyRepaidException(Invoice invoice) {
        super(INVOICE_ALREADY_REPAID, "Invoice with id = " + invoice.getId().toString() + " already repaid");
    }
}
