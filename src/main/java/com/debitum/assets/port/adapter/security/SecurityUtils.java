package com.debitum.assets.port.adapter.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Supplier;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    public static UserAuthDetails getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserAuthDetails) {
                return (UserAuthDetails) authentication.getPrincipal();
            }
        }
        return null;
    }

    public static String getCurrentUserName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserAuthDetails) {
                return ((UserAuthDetails) authentication.getPrincipal()).getName();
            }
        }
        return null;
    }

    /**
     * Throw exception if user not authenticated.
     *
     * @param exceptionSupplier the exception supplier
     */
    public static void throwExceptionIfUserNotAuthenticated(Supplier<RuntimeException> exceptionSupplier){
        UserAuthDetails user = getCurrentUser();
        if(user == null) {
            throw exceptionSupplier.get();
        }
    }
}
