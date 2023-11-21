package com.escolatecnica.api.score.dto;

import java.util.UUID;

public record DTOScoreRequest(UUID materialId, UUID userId, Double value, String comment) {
}
