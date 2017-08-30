package com.debitum.assets.domain.model.user;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class RoleAuthority {

    @Enumerated(EnumType.STRING)
    Authority authority;

    RoleAuthority() {
    }

    public RoleAuthority(Authority authority) {
        this.authority = authority;
    }

    public Authority getAuthority() {
        return authority;
    }
}
