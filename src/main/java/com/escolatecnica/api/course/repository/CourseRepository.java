package com.escolatecnica.api.course.repository;

import com.escolatecnica.api.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    boolean existsByCodeAndNameAndOrganizationId(String code, String name, UUID organizationId);

    Optional<Course> readByIdAndOrganizationId(UUID id, UUID organizationId);

    List<Course> readAllByOrganizationId(UUID organizationId);


}
