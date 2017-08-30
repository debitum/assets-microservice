package com.debitum.assets.domain.model.user;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Role (collection of rights) that can be assigned to user
 */
@Entity
@Table(name = "ROLES")
public class UserRole {

    @Id
    @SequenceGenerator(
            name = "roles_id_seq",
            sequenceName = "roles_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "roles_id_seq"
    )
    private Long id;

    private String title;

    /**
     * The Role authorities.
     */
    @ElementCollection(
            fetch = FetchType.EAGER
    )
    @CollectionTable(
            name = "ROLES_AUTHORITIES",
            joinColumns = @JoinColumn(name = "ROLE_ID")
    )
    @Fetch(FetchMode.SELECT)
    Set<RoleAuthority> roleAuthorities = new HashSet<>();

    UserRole() {
    }

    /**
     * Instantiates a new User role.
     *
     * @param title       the title
     * @param authorities the authorities
     */
    public UserRole(String title, Set<Authority> authorities) {
        this.title = title;
        updateAuthorities(authorities);
    }

    /**
     * Update user role with authorities
     *
     * @param authorities the authorities
     * @return updated user role
     */
    public UserRole updateAuthorities(Set<Authority> authorities) {
        roleAuthorities.clear();
        roleAuthorities.addAll(authorities.stream().map(RoleAuthority::new).collect(Collectors.toSet()));
        return this;
    }

    /**
     * Gets authorities.
     *
     * @return users authorities
     */
    public Set<Authority> getAuthorities() {
        return roleAuthorities.stream().map(RoleAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * Gets user identifier.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets user title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
}
