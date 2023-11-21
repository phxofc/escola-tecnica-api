package com.escolatecnica.api.clazz.model;

import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.root.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "clazz")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "clazz", schema = "public")
@Where(clause = "is_deleted=false")
public class Clazz extends BaseModel {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "is_consolidated", nullable = false)
    private Boolean isConsolidated;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    public Clazz(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt,
            ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String code,
            Boolean isConsolidated, Course course) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.code = code;
        this.isConsolidated = isConsolidated;
        this.course = course;
    }
}
