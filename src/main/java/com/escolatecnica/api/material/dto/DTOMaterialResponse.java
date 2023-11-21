package com.escolatecnica.api.material.dto;

import com.escolatecnica.api.material.model.Type;

import java.util.UUID;

public record DTOMaterialResponse (
    UUID id,
    String title,
    String description,
    Type type,
    String attachedFileName){}
