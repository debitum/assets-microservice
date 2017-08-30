package com.debitum.assets.domain.model.user;


/**
 * Rights that can be assigned to users role
 */
public enum Authority {

    ROLE_AUTHENTICATED,

    ROLE_USERS_EDIT,

    ROLE_USERS_VIEW,

    ROLE_INVESTMENTS_VIEW,

    ROLE_INVESTMENTS_EDIT,

    ROLE_INVOICE_EDIT,

    ROLE_INVOICE_VIEW,

    ROLE_WALLET_OWNER;

    public class Const {
        public static final String ROLE_USERS_VIEW = "ROLE_USERS_VIEW";
        public static final String ROLE_USERS_EDIT = "ROLE_USERS_EDIT";
        public static final String ROLE_INVESTMENTS_VIEW = "ROLE_INVESTMENTS_VIEW";
        public static final String ROLE_INVESTMENTS_EDIT = "ROLE_INVESTMENTS_EDIT";
        public static final String ROLE_INVOICE_EDIT = "ROLE_INVOICE_EDIT";
        public static final String ROLE_INVOICE_VIEW = "ROLE_INVOICE_VIEW";
        public static final String ROLE_AUTHENTICATED = "ROLE_AUTHENTICATED";
        public static final String ROLE_WALLET_OWNER = "ROLE_WALLET_OWNER";

    }
}
