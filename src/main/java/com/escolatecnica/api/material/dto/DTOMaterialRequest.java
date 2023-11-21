package com.escolatecnica.api.material.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record DTOMaterialRequest(
        String title,
        String description,
        String type,
        MultipartFile file,
        UUID disciplineId,
        UUID clazzId,
        UUID courseId) {
}
