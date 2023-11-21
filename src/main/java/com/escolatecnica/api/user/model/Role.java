package com.escolatecnica.api.user.model;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN", "Administrador Geral"),
    ORGANIZATION_ADMIN("ORGANIZATION_ADMIN", "Administrador de Organização"),
    TEACHER("TEACHER", "Professor"),
    STUDENT("STUDENT", "Estudante");

    private final String name;
    private final String description;

    Role(String name, String description){
        this.name = name;
        this.description = description;
    }


}
