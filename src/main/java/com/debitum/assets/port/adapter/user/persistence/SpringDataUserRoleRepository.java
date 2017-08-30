package com.debitum.assets.port.adapter.user.persistence;


import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.domain.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

interface SpringDataUserRoleRepository extends
        JpaRepository<UserRole, Long> {

    @Query("SELECT r FROM User u INNER JOIN u.roles r where u.id = ?1")
    Optional<UserRole> findByUserId(UUID userId);

    Optional<UserRole> findByTitle(String title);

    @Query("SELECT u FROM User u INNER JOIN u.roles r where r.id = ?1")
    Set<User> usersByRoleId(Long roleId);
}
