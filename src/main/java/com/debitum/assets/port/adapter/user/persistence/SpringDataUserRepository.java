package com.debitum.assets.port.adapter.user.persistence;


import com.debitum.assets.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface SpringDataUserRepository extends
        JpaRepository<User, UUID>, JpaSpecificationExecutor {


    /**
     * Finding user with login.
     *
     * @param login
     * @return user with provided login.
     */
    User findByLogin(String login);

    /**
     * Return true if login exists.
     *
     * @param login to check for existence
     * @return existence status
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login = ?1 ")
    boolean existsByLogin(String login);

    /**
     * Returns true if login exists but user id is not in provided list.
     *
     * @param login   to check for existence
     * @param userIds ids list of excluded
     * @return existence status
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login = ?1 and u.id NOT IN ?2")
    boolean existsByLoginAndNotWithIds(String login, List<Long> userIds);
}
