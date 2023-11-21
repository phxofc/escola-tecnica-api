package com.escolatecnica.api.root.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Inheritance
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "organization_id")
    private UUID organizationId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @Column(name = "is_deleted")
    private boolean deleted;

    @Column(name = "registered_by", updatable = false)
    private UUID registeredBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

}
