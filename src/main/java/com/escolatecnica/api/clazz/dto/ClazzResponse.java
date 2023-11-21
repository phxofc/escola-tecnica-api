package com.escolatecnica.api.clazz.dto;

import java.util.UUID;

public record ClazzResponse(UUID id, String code, Boolean isConsolidated, UUID courseId, String courseName) {
}
