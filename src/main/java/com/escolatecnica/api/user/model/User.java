package com.escolatecnica.api.user.model;

import com.escolatecnica.api.answer.model.Answer;
import com.escolatecnica.api.documentation.model.Documentation;
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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "user")
@Table(name = "user", schema = "public")
@Where(clause = "is_deleted=false")
public class User extends BaseModel {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "enabled_access")
    private Boolean enabledAccess;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "documentation_id")
    private Documentation documentation;

    @OneToMany(mappedBy = "user")
    private Set<Answer> answers;

    @Builder
    public User(UUID id, UUID organizationId, ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt,
            boolean deleted, UUID registeredBy, UUID updatedBy, String name, String lastName, String cpf, String email,
            String password, Role role, Boolean enabledAccess, Documentation documentation) {
        super(id, organizationId, createdAt, updatedAt, deletedAt, deleted, registeredBy, updatedBy);
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabledAccess = enabledAccess;
        this.documentation = documentation;
    }

    @Override
    public String toString() {
        return this.name + ' ' + this.lastName;
    }

}
