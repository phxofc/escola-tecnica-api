package com.escolatecnica.api.course.model;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.enrollment.model.Enrollment;
import com.escolatecnica.api.root.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "course", schema = "public")
@Where(clause = "is_deleted=false")
public class Course extends BaseModel {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "course")
    private Set<Enrollment> registrations;

    @OneToMany(mappedBy = "course")
    private Set<Discipline> disciplines;

    @OneToMany(mappedBy = "course")
    private Set<Clazz> clazzes;

    @Builder
    public Course(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String code, String name) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
