package com.escolatecnica.api.enrollment.repository;

import com.escolatecnica.api.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> readByOrganizationIdAndUserId(UUID organizationId, UUID id);

    @Query("select e from enrollment e where e.organizationId = ?1 and e.course.id = ?2 and e.clazz.id = ?3 and e.user.id = ?4")
    Enrollment findByCourseAndClazzAndStudentAndOrganization(UUID organizationId, UUID courseId, UUID clazzId, UUID studentId);

}
