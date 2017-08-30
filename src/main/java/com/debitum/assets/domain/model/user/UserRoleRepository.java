package com.debitum.assets.domain.model.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRoleRepository {

    /**
     * Finds user role entity by user id.
     *
     * @param userId
     * @return
     */
    Optional<UserRole> findUserRole(UUID userId);

    /**
     * Saves UserRole if it meets requirements/preconditions.
     *
     * @param userRole unique user role entity.
     * @return saved user role.
     */
    UserRole save(UserRole userRole);

    /**
     * Finds existing user role.
     *
     * @param id existing user role id.
     * @return existing user role.
     */
    UserRole findOne(Long id);

    /**
     * Finds users that have given role id.
     *
     * @param id of role
     * @return set of users with given role id.
     */
    Set<User> usersByRoleId(Long id);

    /**
     *
     * @return all existing user roles.
     */
    List<UserRole> findAll();

    /**
     * Deletes from database existing entry.
     *
     * @param id of user role to delete.
     */
    void delete(Long id);
}
