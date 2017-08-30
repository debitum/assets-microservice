package com.debitum.assets.domain.model.user.exception;


import com.debitum.assets.domain.model.DomainException;

public class UserRoleNotFoundException extends DomainException {

    public static final String USER_ROLE_NOT_FOUND = "USER_ROLE_NOT_FOUND";

    public UserRoleNotFoundException(Long id) {
        super(USER_ROLE_NOT_FOUND, "User role with id {0} was not found", id);
    }
}
