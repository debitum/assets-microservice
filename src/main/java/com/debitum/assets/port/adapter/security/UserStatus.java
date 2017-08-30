package com.debitum.assets.port.adapter.security;

/**
 * Possible user statuses
 */
public enum UserStatus {

    /**
     * Active - user successfully registered and activated by email.
     */
    ACTIVE,

    /**
     * User is deactivated by admin
     */
    INACTIVE,

    /**
     * User is registered but not activated by email.
     */
    PENDING
}
