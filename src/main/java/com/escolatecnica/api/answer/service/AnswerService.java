package com.escolatecnica.api.answer.service;

import com.escolatecnica.api.answer.model.Answer;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AnswerService {

    boolean create(Answer answer, MultipartFile file, UUID userId, UUID materialId, UUID organizationId) throws APIException, NotFoundException;

    boolean thereIsAnswerAssociatedWithMaterial(UUID userId, UUID materialId, UUID organizationId) throws APIException;

    List<Answer> readAllAnswerFromTask(UUID materialId, UUID organizationId) throws APIException;

}
