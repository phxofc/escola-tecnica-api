package com.escolatecnica.api.answer.dto;

import com.escolatecnica.api.score.dto.DTOScoreResponse;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DTOAnswerResponse(
        UUID userId,
        String userFullName,
        String description,
        String attachedFileName,
        ZonedDateTime sendDate,
        DTOScoreResponse score
        ) {
}
