package com.escolatecnica.api.enrollment.model;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.root.model.BaseModel;
import com.escolatecnica.api.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "enrollment")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "enrollment", schema = "public")
@Where(clause = "is_deleted=false")
public class Enrollment extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "clazz_id", nullable = false)
    private Clazz clazz;

    @Column(name = "registration_approved", nullable = false)
    private Boolean registrationApproved;

    @Builder

    public Enrollment(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, User user, Course course, Clazz clazz, Boolean registrationApproved) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.user = user;
        this.course = course;
        this.clazz = clazz;
        this.registrationApproved = registrationApproved;
    }
}
