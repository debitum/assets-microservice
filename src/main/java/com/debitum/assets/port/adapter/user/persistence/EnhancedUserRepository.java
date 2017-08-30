package com.debitum.assets.port.adapter.user.persistence;


import com.debitum.assets.application.user.UserRoleApplicationService;
import com.debitum.assets.domain.model.Preconditions;
import com.debitum.assets.domain.model.user.*;

import com.debitum.assets.port.adapter.helpers.SpecificationFilter;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.debitum.assets.port.adapter.user.persistence.EnhancedUserRepository.UserFilterSpecification.*;
import static com.google.common.collect.Lists.newArrayList;

@Component
class EnhancedUserRepository implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserRoleApplicationService userRoleApplicationService;

    EnhancedUserRepository(
            SpringDataUserRepository springDataUserRepository,
            UserRoleApplicationService userRoleApplicationService) {
        this.springDataUserRepository = springDataUserRepository;
        this.userRoleApplicationService = userRoleApplicationService;
    }
    @Override
    public User save(User user) {
        User existing = springDataUserRepository.findByLogin(user.getLogin());
        Preconditions.isTrue(
                existing == null
                        || existing.getId().equals(user.getId()),
                new DuplicateLoginException(user.getLogin())
        );
        User newUser = springDataUserRepository.save(user);
        return newUser;
    }

    @Override
    public List<User> findAll(UserFilter userFilter) {
        return springDataUserRepository.findAll(of(userFilter));
    }

    @Override
    public User get(UUID id) {
        return springDataUserRepository.getOne(id);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll();
    }

    @Override
    public UserDetails findUserByLogin(String login) {
        User user = springDataUserRepository.findByLogin(login);
        if(user == null) return null;

        Optional<UserRole> role = userRoleApplicationService.findUserRole(user.getId());
        return new UserDetails(user, role);
    }

    @Override
    public boolean existsByLoginAndNotWithIds(String login, Long... userIds) {
        if (userIds != null
                && userIds.length > 0) {
            return springDataUserRepository.existsByLoginAndNotWithIds(login, newArrayList(userIds));
        } else {
            return springDataUserRepository.existsByLogin(login);
        }
    }

    static class UserFilterSpecification extends SpecificationFilter<UserFilter> {

        private UserFilterSpecification(UserFilter filter) {
            Validate.notNull(filter);

            filter.getName().ifPresent(s ->
                    addSpec((root, query, cb) -> cb.like(
                            cb.lower(root.get("name")),
                            "%" + s.toLowerCase() + "%"
                    ))
            );

            filter.getStates().ifPresent(s ->
                    addSpec((root, query, cb) -> root.get("status").in(s))
            );
        }

        static SpecificationFilter<UserFilter> of(UserFilter filter) {
            return new UserFilterSpecification(filter);
        }
    }
}
