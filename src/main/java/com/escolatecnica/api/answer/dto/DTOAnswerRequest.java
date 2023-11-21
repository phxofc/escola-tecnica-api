package com.escolatecnica.api.answer.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record DTOAnswerRequest(UUID materialId, String description, MultipartFile file) {
}
