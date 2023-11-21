package com.escolatecnica.api.clazz.service;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface ClazzService {

    boolean create(Clazz clazz, UUID courseId, UUID organizationId) throws APIException, NotFoundException;

    boolean update(Clazz clazz, UUID courseId, UUID organizationId) throws APIException, NotFoundException;

    boolean detele(UUID organizationId, UUID clazzId) throws APIException, NotFoundException;

    List<Clazz> readByNonConsolidation(UUID organizationId, UUID id) throws APIException;

    List<Clazz> readAllByOrganizationId(UUID organizationId) throws APIException;

    Clazz readByOrganizationIdAndId(UUID organizationId, UUID id) throws APIException;

    boolean manageConsolidation(UUID organizationId, UUID clazzId) throws APIException, NotFoundException;

    List<Clazz> readAllClazzsByTeacher(UUID courseId, UUID disciplineId, UUID userId, UUID organizationId)
            throws APIException;

    boolean existsClazz(UUID organizationId, String code, UUID id);

}
