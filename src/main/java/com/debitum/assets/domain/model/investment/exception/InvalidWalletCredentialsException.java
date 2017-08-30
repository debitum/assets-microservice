package com.debitum.assets.domain.model.investment.exception;


import com.debitum.assets.domain.model.DomainException;

    public class InvalidWalletCredentialsException extends DomainException {

        /**
         * Exception code.
         */
        public static final String INVALID_WALLET_CREDENTIALS = "INVALID_WALLET_CREDENTIALS";

        /**
         * Instantiates a new Domain access exception.
         */
        public InvalidWalletCredentialsException() {
            super(INVALID_WALLET_CREDENTIALS, "Invalid provided wallet keystore or password");
        }

}
