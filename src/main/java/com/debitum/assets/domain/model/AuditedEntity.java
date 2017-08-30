package com.debitum.assets.domain.model;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.Instant;

/**
 * Audited entity
 */
@MappedSuperclass
public abstract class AuditedEntity {

    @Column(
            name = "CREATED_TIMESTAMP_UTC",
            updatable = false
    )
    protected Instant createdOn;

    @Column(name = "UPDATED_TIMESTAMP_UTC")
    protected Instant updatedOn;


    protected AuditedEntity() {
        setInitialTimestamps();
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

    /**
     * @return creation date
     */
    public Instant getCreatedOn() {
        return createdOn;
    }

    /**
     * @return last update date
     */
    public Instant getUpdatedOn() {
        return updatedOn;
    }


}
