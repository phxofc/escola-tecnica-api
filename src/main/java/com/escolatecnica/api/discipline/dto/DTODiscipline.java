package com.escolatecnica.api.discipline.dto;

import java.util.UUID;

public record DTODiscipline(UUID id, String code, String name, UUID courseId) {
}
