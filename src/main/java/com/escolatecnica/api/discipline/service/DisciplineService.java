package com.escolatecnica.api.discipline.service;

import com.escolatecnica.api.discipline.dto.DTODisciplineScore;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface DisciplineService {
    UUID create(Discipline discipline, UUID courseId, UUID organizationId) throws APIException, NotFoundException;

    UUID update(Discipline discipline, UUID courseId, UUID organizationId);

    Discipline readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException;

    boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException;

    List<Discipline> readAllByOrganizationId(UUID organizationId) throws APIException;

    List<Discipline> readAllByCourseId(UUID organizationId, UUID id) throws APIException;

    List<Discipline> readAllByCourseIdAndTeacherIdAndOrganizationId(UUID courseId, UUID userId, UUID organizationId);

    List<DTODisciplineScore> listBulletin(UUID courseId, UUID clazzId, UUID studentId, UUID organizationId) throws APIException;
}
