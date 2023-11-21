package com.escolatecnica.api.discipline.repository;

import com.escolatecnica.api.discipline.dto.DTODisciplineScore;

import java.util.List;
import java.util.UUID;

public interface DisciplineCustomRepository {

    List<DTODisciplineScore> listDisciplinesAndScores(UUID courseId, UUID clazzId, UUID studentId, UUID organizationId);
}
