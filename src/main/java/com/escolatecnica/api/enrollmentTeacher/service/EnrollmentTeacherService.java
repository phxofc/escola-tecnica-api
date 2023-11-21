package com.escolatecnica.api.enrollmentTeacher.service;

import java.util.List;
import java.util.UUID;

import com.escolatecnica.api.enrollmentTeacher.model.EnrollmentTeacher;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;

public interface EnrollmentTeacherService {
    boolean create(UUID courseId,
            UUID clazzId,
            UUID disciplineId,
            UUID teacherId,
            UUID organizationId,
            UUID registryBy) throws APIException, NotFoundException;

    List<EnrollmentTeacher> readAllByOrganizationId(UUID organizationId) throws APIException;

    EnrollmentTeacher readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException;

    EnrollmentTeacher readByCourseAndDisciplineAndClazzAndUserAndOrganizationId(UUID idCourse, UUID idDiscipline, UUID idClazz, UUID idTeacher, UUID organizationId);

}
