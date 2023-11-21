package com.escolatecnica.api.documentation.model;


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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "documentation")
@Table(name = "documentation", schema = "public")
@Where(clause = "is_deleted=false")
public class Documentation extends BaseModel {

    @Column(name = "cpf_file_name")
    private String cpfFileName;

    @Column(name = "rg_file_name")
    private String rgFileName;

    @Column(name = "cr_file_name")
    private String crFileName;

    @Column(name = "validated")
    private Boolean validated;

    @OneToOne(mappedBy = "documentation")
    private User user;

    @Builder
    public Documentation(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt, boolean deleted, UUID registeredBy, UUID updatedBy, String cpfFileName, String rgFileName, String crFileName, Boolean validated) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.cpfFileName = cpfFileName;
        this.rgFileName = rgFileName;
        this.crFileName = crFileName;
        this.validated = validated;
    }
}
