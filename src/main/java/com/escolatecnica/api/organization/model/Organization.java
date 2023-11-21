package com.escolatecnica.api.organization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "organization")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "organization", schema = "public")
@Where(clause = "is_deleted=false")
public class Organization {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

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

    @Column(name = "is_root", nullable = false)
    private Boolean isRoot;
}
