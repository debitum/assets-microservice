package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;

public class DuplicateRoleTitleException extends DomainException {

    public static final String ROLE_TITLE_IS_NOT_UNIQUE = "ROLE_TITLE_IS_NOT_UNIQUE";

    public DuplicateRoleTitleException(String roleTitle) {
        super(ROLE_TITLE_IS_NOT_UNIQUE, "Users role title {0} already exists", roleTitle);
    }
}
