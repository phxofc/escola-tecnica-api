package com.escolatecnica.api.score.model;

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

@Entity(name = "score")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "score", schema = "public")
@Where(clause = "is_deleted=false")
public class Score extends BaseModel {

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name="material_id", nullable=false)
    private Material material;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Builder
    public Score(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, Double value, String comment, Material material, User user) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.value = value;
        this.comment = comment;
        this.material = material;
        this.user = user;
    }
}
