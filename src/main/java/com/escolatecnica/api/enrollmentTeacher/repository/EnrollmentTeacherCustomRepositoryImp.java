package com.escolatecnica.api.enrollmentTeacher.repository;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.discipline.model.Discipline;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EnrollmentTeacherCustomRepositoryImp implements EnrollmentTeacherCustomRepository{

    private final EntityManager entityManager;

    @Override
    public List<Course> readAllCoursesByTeacher(UUID userId, UUID organizationId) {

        String queryString = "SELECT c FROM course as c INNER JOIN enrollment_teacher as et ON c.id = et.course.id AND et.user.id = :userId AND et.organizationId = :organizationId";

        Query query = entityManager.createQuery(queryString, Course.class);

        if (Objects.nonNull(userId)) {
            query.setParameter("userId", userId);
        }

        if (Objects.nonNull(organizationId)) {
            query.setParameter("organizationId", organizationId);
        }

        return query.getResultList();
    }

    @Override
    public List<Discipline> readAllDisciplinesByTeacher(UUID courseId, UUID userId, UUID organizationId) {
        String queryString = "SELECT d FROM discipline as d INNER JOIN enrollment_teacher as et ON d.id = et.discipline.id AND et.course.id = :courseId AND et.user.id = :userId AND et.organizationId = :organizationId";

        Query query = entityManager.createQuery(queryString, Discipline.class);

        if (Objects.nonNull(courseId)) {
            query.setParameter("courseId", courseId);
        }

        if (Objects.nonNull(userId)) {
            query.setParameter("userId", userId);
        }

        if (Objects.nonNull(organizationId)) {
            query.setParameter("organizationId", organizationId);
        }

        return query.getResultList();
    }

    @Override
    public List<Clazz> readAllClazzsByTeacher(UUID courseId, UUID disciplineId, UUID userId, UUID organizationId) {
        String queryString = "SELECT cl FROM clazz as cl INNER JOIN enrollment_teacher as et ON cl.id = et.clazz.id AND et.course.id = :courseId AND et.discipline.id = :disciplineId AND et.user.id = :userId AND et.organizationId = :organizationId";

        Query query = entityManager.createQuery(queryString, Clazz.class);

        if (Objects.nonNull(courseId)) {
            query.setParameter("courseId", courseId);
        }

        if (Objects.nonNull(disciplineId)) {
            query.setParameter("disciplineId", disciplineId);
        }

        if (Objects.nonNull(userId)) {
            query.setParameter("userId", userId);
        }

        if (Objects.nonNull(organizationId)) {
            query.setParameter("organizationId", organizationId);
        }

        return query.getResultList();
    }
}
