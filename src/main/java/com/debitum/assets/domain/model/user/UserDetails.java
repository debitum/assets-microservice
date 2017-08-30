package com.debitum.assets.domain.model.user;

import java.util.Optional;
import java.util.Set;

import static com.debitum.assets.domain.model.user.UserStatus.ACTIVE;
import static com.google.common.collect.Sets.newHashSet;


/**
 * Debitum user information with authorities
 */
public class UserDetails {

    private User user;
    private Set<Authority> authorities;

    public UserDetails(User user, Optional<UserRole> role) {
        this.user = user;
        setAuthoritiesFromRole(role);
    }

    /**
     * @return Debitum user
     */
    public User getUser() {
        return user;
    }

    /**
     * @return users authority. If user is inactive, then it has no authorities
     */
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    private void setAuthoritiesFromRole(Optional<UserRole> role) {
        if (role.isPresent()
                && user.getStatus() == ACTIVE) {
            this.authorities = role.get().getAuthorities();
        } else {
            this.authorities = newHashSet();
        }

    }
}
