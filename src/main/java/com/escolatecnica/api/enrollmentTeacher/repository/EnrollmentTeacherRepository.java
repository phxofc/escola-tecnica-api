package com.escolatecnica.api.enrollmentTeacher.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escolatecnica.api.enrollmentTeacher.model.EnrollmentTeacher;

public interface EnrollmentTeacherRepository extends JpaRepository<EnrollmentTeacher, UUID> {

    List<EnrollmentTeacher> findByOrganizationId(UUID organizationId);

    EnrollmentTeacher findByIdAndOrganizationId(UUID id, UUID organizationId);

    EnrollmentTeacher findByCourse_IdAndDiscipline_IdAndClazz_IdAndUser_IdAndOrganizationId(UUID idCourse, UUID idDiscipline, UUID idClazz, UUID idTeacher, UUID organizationId);

}
