package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;

public class WalletBalanceNotEnoughException extends DomainException {

    /**
     * Exception code.
     */
    public static final String WALLET_BALANCE_NOT_ENOUGH = "WALLET_BALANCE_NOT_ENOUGH";

    /**
     * Instantiates a new Domain access exception.
     */
    public WalletBalanceNotEnoughException() {
        super(WALLET_BALANCE_NOT_ENOUGH, "Wallet balance is not enough");
    }
}
