package com.escolatecnica.api.enrollmentTeacher.dto;

import java.util.UUID;

public record DTOResponse(
        UUID id,
        String teacherName,
        String disciplineName,
        String courseName,
        String clazzCode) {
}
