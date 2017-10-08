package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;

public class InvestmentNotSentToBlockchainException extends DomainException {

    /**
     * Exception code.
     */
    public static final String INVESTMENT_NOT_SENT_TO_BLOCKCHAIN_YET = "INVESTMENT_NOT_SENT_TO_BLOCKCHAIN_YET";

    /**
     * Instantiates a new Domain access exception.
     */
    public InvestmentNotSentToBlockchainException() {
        super(INVESTMENT_NOT_SENT_TO_BLOCKCHAIN_YET, "Investment not sent to blockchain. The transaction needs about 5-10 min.");
    }
}
