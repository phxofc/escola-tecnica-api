package com.escolatecnica.api.discipline.model;

import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.root.model.BaseModel;
import com.escolatecnica.api.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "discipline")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "discipline", schema = "public")
@Where(clause = "is_deleted=false")
public class Discipline extends BaseModel {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    public Discipline(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String code, String name, Course course) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.code = code;
        this.name = name;
        this.course = course;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
