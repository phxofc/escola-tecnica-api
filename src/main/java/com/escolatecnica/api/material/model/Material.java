package com.escolatecnica.api.material.model;

import com.escolatecnica.api.answer.model.Answer;
import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.root.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "material")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "material", schema = "public")
@Where(clause = "is_deleted=false")
public class Material extends BaseModel {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "attached_file_name")
    private String attachedFileName;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy="material")
    private Set<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "discipline_id")
    private Discipline discipline;

    @ManyToOne
    @JoinColumn(name = "clazz_id")
    private Clazz clazz;

    @Builder
    public Material(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String title, String description, Type type, String attachedFileName, Boolean isActive, Course course, Discipline discipline, Clazz clazz) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.title = title;
        this.description = description;
        this.type = type;
        this.attachedFileName = attachedFileName;
        this.isActive = isActive;
        this.course = course;
        this.clazz = clazz;
        this.discipline = discipline;
    }
}
