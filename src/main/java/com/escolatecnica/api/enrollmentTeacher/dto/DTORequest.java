package com.escolatecnica.api.enrollmentTeacher.dto;

import java.util.UUID;

public record DTORequest(
        UUID id,
        UUID courseId,
        UUID clazzId,
        UUID disciplineId,
        UUID teacherId) {

}
