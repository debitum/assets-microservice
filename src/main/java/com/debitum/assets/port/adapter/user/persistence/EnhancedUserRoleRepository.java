package com.debitum.assets.port.adapter.user.persistence;


import com.debitum.assets.domain.model.Preconditions;
import com.debitum.assets.domain.model.user.User;
import com.debitum.assets.domain.model.user.UserRole;
import com.debitum.assets.domain.model.user.UserRoleRepository;
import com.debitum.assets.domain.model.user.exception.DuplicateRoleTitleException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
class EnhancedUserRoleRepository implements UserRoleRepository {

    private final SpringDataUserRoleRepository springDataUserRoleRepository;

    EnhancedUserRoleRepository(SpringDataUserRoleRepository springDataUserRoleRepository) {
        this.springDataUserRoleRepository = springDataUserRoleRepository;
    }

    @Override
    public Optional<UserRole> findUserRole(UUID userId) {
        return springDataUserRoleRepository.findByUserId(userId);
    }

    @Override
    public UserRole save(UserRole userRole) {
        Optional<UserRole> existing = springDataUserRoleRepository.findByTitle(userRole.getTitle());
        Preconditions.isTrue(
                !existing.isPresent()
                        || existing.get().getId().equals(userRole.getId()),
                new DuplicateRoleTitleException(userRole.getTitle())
        );
        return springDataUserRoleRepository.save(userRole);
    }

    @Override
    public UserRole findOne(Long id) {
        return springDataUserRoleRepository.findOne(id);
    }

    @Override
    public Set<User> usersByRoleId(Long id) {
        return springDataUserRoleRepository.usersByRoleId(id);
    }


    @Override
    public List<UserRole> findAll() {
        return springDataUserRoleRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        springDataUserRoleRepository.delete(id);
    }
}
