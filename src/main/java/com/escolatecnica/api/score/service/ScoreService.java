package com.escolatecnica.api.score.service;

import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.score.model.Score;

import java.util.UUID;

public interface ScoreService {

    boolean thereIsANoteForThisTask(UUID materialId, UUID userId, UUID organizationId);

    boolean create(Score score, UUID materialId, UUID userId, UUID organizationId) throws APIException;

    Score readByMaterialIdAndUserIdAndOrganizationId(UUID materialId, UUID userId, UUID organizationId) throws APIException;

}
