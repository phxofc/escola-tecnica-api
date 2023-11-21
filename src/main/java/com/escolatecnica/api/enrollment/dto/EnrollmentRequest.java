package com.escolatecnica.api.enrollment.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record EnrollmentRequest(
        UUID organizationId,
        UUID courseId,
        UUID clazzId,
        MultipartFile[] files,
        String name,
        String lastName,
        String cpf,
        String password,
        String email) {
}
