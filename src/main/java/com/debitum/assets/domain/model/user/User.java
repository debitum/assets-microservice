package com.debitum.assets.domain.model.user;

import com.debitum.assets.domain.model.AuditedEntity;
import com.debitum.assets.domain.model.Preconditions;
import com.debitum.assets.domain.model.event.DomainEventPublisher;
import com.debitum.assets.domain.model.user.events.ActivationLinkResendActivated;
import com.debitum.assets.domain.model.user.events.UserCreated;
import com.debitum.assets.domain.model.user.exception.InvalidUserStatusTransitionException;
import com.debitum.assets.domain.model.user.exception.PasswordIncorrectException;
import com.debitum.assets.domain.model.user.exception.UserAlreadyActivatedException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.debitum.assets.domain.model.user.UserStatus.ACTIVE;
import static com.debitum.assets.domain.model.user.UserStatus.INACTIVE;

/**
 * Users that can authenticate to the system
 */
@Entity
@Table(name = "USERS")
public class User extends AuditedEntity {



    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    private String login;

    private String phone;

    private String company;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @Fetch(FetchMode.SELECT)
    private Set<Credential> credentials = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private Set<UserRole> roles = new HashSet<>();



    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected User() {
    }

    public User(
            String login,
            String phone,
            String company,
            String name,
            UserRole role
    ) {
        setLogin(login);
        setPhone(phone);
        setCompany(company);
        setName(name);
        setUserRole(role);
        credentials.add(new Credential());
        this.status = INACTIVE;
        DomainEventPublisher.publish(new UserCreated(this));
    }

    /**
     * @return users identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return users login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param phone   users phone number
     * @param company users company
     * @param name    users name
     * @return updated user
     */
    public User updateUserInfo(String phone,
                               String company,
                               String name) {
        setPhone(phone);
        setCompany(company);
        setName(name);

        return this;
    }

    /**
     * @param newPassword update users password
     * @return updated user
     */
    public User updatePassword(String newPassword) {
        Validate.isTrue(newPassword.length() >= 8, "Password should be at least 8 symbols long. Please try again.");
        credentials.stream().findFirst().ifPresent(credential -> credential.updatePassword(passwordEncoder.encode(newPassword)));
        return this;
    }

    /**
     * @param oldPassword current password for check
     * @param newPassword new user password which will be updated
     * @return updated user
     */
    public User updatePassword(String oldPassword, String newPassword) {
        Preconditions.isTrue(passwordEncoder.matches(oldPassword, getPassword()), new PasswordIncorrectException());

        return updatePassword(newPassword);
    }

    /**
     * @return users password
     */
    public String getPassword() {
        if (CollectionUtils.isEmpty(credentials)) {
            return null;
        }
        return credentials.stream().findFirst().get().getPassword();
    }

    /**
     * @param newPassword update users password
     * @return updated user
     */
    public User setInitialPassword(String newPassword) {
        this.status = ACTIVE;

        return updatePassword(newPassword);
    }

    private void setLogin(String login) {
        Validate.notNull(
                login,
                "Login is mandatory for user"
        );
        this.login = login;
    }

    /**
     * @return users phone number
     */
    public String getPhone() {
        return phone;
    }

    private void setPhone(String phone) {
        Validate.notNull(
                phone,
                "Phone is mandatory for user"
        );
        this.phone = phone;
    }

    /**
     * @return users company
     */
    public String getCompany() {
        return company;
    }

    private void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return users name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return users status
     */
    public UserStatus getStatus() {
        return status;
    }

    private void setStatus(UserStatus status) {
        this.status = status;
    }



    public void resendPasswordActivationLink() {
        Preconditions.isTrue(
                getPassword() == null,
                new UserAlreadyActivatedException(getLogin())
        );
        DomainEventPublisher.publish(new ActivationLinkResendActivated(this));
    }

    public void changeStatus(UserStatus status) {
        if (status != this.status && !UserStatus.availableTransitions(this.status).contains(status)) {
            throw new InvalidUserStatusTransitionException(this.status, status);
        }

        this.status = status;
    }

    private User setUserRole(UserRole role) {
        Validate.notNull(
                role,
                "Role is mandatory for user"
        );
        this.roles.clear();
        this.roles.add(role);
        return this;
    }


}
