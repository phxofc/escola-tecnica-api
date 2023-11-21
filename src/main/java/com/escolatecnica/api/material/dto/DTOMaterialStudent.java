package com.escolatecnica.api.material.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.escolatecnica.api.material.model.Type;

public record DTOMaterialStudent(
        UUID id,
        String title,
        String description,
        String typeString,
        Boolean isActive,
        String attachedFileName,
        ZonedDateTime createdAt,
        Type type,
        Boolean alreadySentAnswer) {
}
