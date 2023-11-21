package com.escolatecnica.api.material.model;

import lombok.Getter;

@Getter
public enum Type {

    SUPPORT("SUPPORT", "Material de Suporte"),
    EVALUATION("EVALUATION", "Material Avaliativo");

    private final String name;
    private final String description;

    Type(String name, String description){
        this.name = name;
        this.description = description;
    }
}
