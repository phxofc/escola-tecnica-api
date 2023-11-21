package com.escolatecnica.api.enrollmentTeacher.model;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.root.model.BaseModel;
import com.escolatecnica.api.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "enrollment_teacher")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "enrollment_teacher", schema = "public")
@Where(clause = "is_deleted=false")
public class EnrollmentTeacher extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "clazz_id")
    private Clazz clazz;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public EnrollmentTeacher(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, Course course, Discipline discipline, Clazz clazz, User user) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.course = course;
        this.discipline = discipline;
        this.clazz = clazz;
        this.user = user;
    }
}
