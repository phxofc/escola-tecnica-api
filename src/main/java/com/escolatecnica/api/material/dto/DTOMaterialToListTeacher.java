package com.escolatecnica.api.material.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.escolatecnica.api.material.model.Type;

public record DTOMaterialToListTeacher(
                UUID id,
                String title,
                String description,
                Type type,
                String typeString,
                Boolean isActive,
                String attachedFileName,
                ZonedDateTime createdAt,
                String courseName,
                String clazzCode,
                String disciplineName) {
}
