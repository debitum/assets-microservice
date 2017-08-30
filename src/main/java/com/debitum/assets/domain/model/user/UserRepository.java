package com.debitum.assets.domain.model.user;


import com.debitum.assets.port.adapter.user.persistence.UserFilter;

import java.util.List;
import java.util.UUID;

/**
 * Local users repository
 */
public interface UserRepository {

    User save(User user);

    User get(UUID id);

    List<User> findAll();

    List<User> findAll(UserFilter userFilter);

    UserDetails findUserByLogin(String login);

    /**
     * @param login   users login
     * @param userIds users, which is not included in search, identifiers
     * @return if users login is used
     */
    boolean existsByLoginAndNotWithIds(String login, Long... userIds);



}
