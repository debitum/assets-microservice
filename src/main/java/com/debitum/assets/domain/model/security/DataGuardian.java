package com.debitum.assets.domain.model.security;


import com.debitum.assets.domain.model.security.exception.DomainAccessException;
import com.debitum.assets.port.adapter.security.SecurityUtils;
import com.debitum.assets.port.adapter.security.UserAuthDetails;

import java.util.List;
import java.util.stream.Collectors;

import static com.debitum.assets.port.adapter.security.SecurityUtils.throwExceptionIfUserNotAuthenticated;


/**
 * Data guardian.
 */
public class DataGuardian {

    /**
     * Filter domain resource list to pass only that user belonging data which user manages.
     *
     * @param <T>  the type of domain resource
     * @param data the domain data
     * @return the filtered domain resources
     */
    public <T extends UsersProperty> List<T> filter(List<T> data) {
        throwExceptionIfUserNotAuthenticated(DomainAccessException::new);

        UserAuthDetails currentUser = SecurityUtils.getCurrentUser();

        return data.stream()
                .filter(c -> c.getUserId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Check access to users domain resources.
     *
     * @param data the users domain resource
     * @throws DomainAccessException the domain access exception which is thrown
     *                               if user doesn't manage resource to who's data try to access
     */
    public void checkAccessTo(UsersProperty data) throws DomainAccessException {
        throwExceptionIfUserNotAuthenticated(DomainAccessException::new);

        UserAuthDetails currentUser = SecurityUtils.getCurrentUser();


        if (!currentUser.getId().equals(data.getUserId())) {
            throw new DomainAccessException();
        }
    }


}
