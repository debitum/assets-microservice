package com.debitum.assets.domain.model.user;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.Validate;

import javax.persistence.*;
import java.time.Instant;

/**
 * users credentials
 */
@Entity
@Table(name = "USER_CREDENTIAL")
public class Credential {

    @Id
    @SequenceGenerator(
            name = "users_credential_id_seq",
            sequenceName = "users_credential_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_credential_id_seq"
    )
    private Long id;

    private String password;


    @Column(
            name = "CREATED_TIMESTAMP_UTC",
            updatable = false
    )
    private Instant createdOn;

    @Column(name = "UPDATED_TIMESTAMP_UTC")
    private Instant updatedOn;

    public Credential() {
        setInitialTimestamps();
    }

    /**
     *
     * @return users password
     */
    public String getPassword() {
        return password;
    }


    /**
     *
     * @return credentials creation date
     */
    public Instant getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @return credentials last update date
     */
    public Instant getUpdatedOn() {
        return updatedOn;
    }


    Credential updatePassword(String newPassword) {
        Validate.notEmpty(
                newPassword,
                "Password is mandatory for user"
        );
        this.password = newPassword;
        return this;
    }

    private void setInitialTimestamps() {
        Instant now = Instant.now();
        createdOn = now;
        updatedOn = now;
    }

    @PreUpdate
    public void setUpdatedOn() {
        this.updatedOn = Instant.now();
    }
}
