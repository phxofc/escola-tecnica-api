package com.escolatecnica.api.answer.model;

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
import java.util.UUID;

@Entity(name = "answer")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "answer", schema = "public")
@Where(clause = "is_deleted=false")
public class Answer extends BaseModel {

    @Column(name = "description")
    private String description;

    @Column(name = "attached_file_name")
    private String attachedFileName;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="material_id", nullable = false)
    private Material material;

    @Builder
    public Answer(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String description, String attachedFileName, User user, Material material) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.description = description;
        this.attachedFileName = attachedFileName;
        this.user = user;
        this.material = material;
    }
}
