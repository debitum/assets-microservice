package com.debitum.assets.domain.model.user.activations;


/**
 * Token keys for different actions
 */
public enum KeyType {

    /**
     * Token key for initial password setup, after user
     * is created in DEBITUM system
     */
    INITIAL_PASSWORD_SET,

    LOGIN,


    /**
     * Token key for password reminding
     */
    PASSWORD_REMIND
}
