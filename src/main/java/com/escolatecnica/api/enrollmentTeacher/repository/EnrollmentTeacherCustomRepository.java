package com.escolatecnica.api.enrollmentTeacher.repository;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.discipline.model.Discipline;

import java.util.List;
import java.util.UUID;

public interface EnrollmentTeacherCustomRepository {

    List<Course> readAllCoursesByTeacher(UUID userId, UUID organizationId);

    List<Discipline> readAllDisciplinesByTeacher(UUID courseId, UUID userId, UUID organizationId);

    List<Clazz> readAllClazzsByTeacher(UUID courseId, UUID disciplineId, UUID userId, UUID organizationId);
}
