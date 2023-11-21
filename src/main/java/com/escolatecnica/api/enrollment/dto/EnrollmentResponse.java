package com.escolatecnica.api.enrollment.dto;

import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        String courseName,
        Boolean freeAccess,
        String clazzCode,
        UUID courseId,
        UUID clazzId) {
}
