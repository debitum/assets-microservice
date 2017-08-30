package com.debitum.assets.application.user;


import com.debitum.assets.domain.model.user.UserRole;
import com.debitum.assets.domain.model.user.UserRoleRepository;
import com.debitum.assets.domain.model.user.exception.UserRoleNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * User role application service
 * Actions only with roles
 */
@Component
@Transactional
public class UserRoleApplicationService {

    private final UserRoleRepository userRoleRepository;

    /**
     * Instantiates a new User role application service.
     *
     * @param userRoleRepository the user role repository
     */
    public UserRoleApplicationService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * Finds user role by user identifier
     *
     * @param userId user identifier
     * @return the found user role or empty optional
     */
    public Optional<UserRole> findUserRole(UUID userId) {
        return userRoleRepository.findUserRole(userId);
    }

    /**
     * Gets existing user role by it's identifier
     *
     * @param roleId role identifier
     * @return found user role
     */
    public UserRole get(Long roleId) {
        UserRole userRole = userRoleRepository.findOne(roleId);
        if (userRole == null) {
            throw new UserRoleNotFoundException(roleId);
        } else {
            return userRole;
        }
    }


}
