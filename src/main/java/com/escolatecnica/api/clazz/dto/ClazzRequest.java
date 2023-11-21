package com.escolatecnica.api.clazz.dto;

import java.util.UUID;

public record ClazzRequest(UUID id, String code, UUID courseId) {
}
