package com.escolatecnica.api.discipline.service;

import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.service.CourseService;
import com.escolatecnica.api.discipline.dto.DTODisciplineScore;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.discipline.repository.DisciplineCustomRepository;
import com.escolatecnica.api.discipline.repository.DisciplineRepository;
import com.escolatecnica.api.enrollmentTeacher.repository.EnrollmentTeacherCustomRepository;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DisciplineServiceImp implements DisciplineService {

    private final CourseService courseService;

    private final DisciplineRepository repository;

    private final DisciplineCustomRepository customRepository;

    private final EnrollmentTeacherCustomRepository enrollmentTeacherCustomRepository;

    @Override
    public UUID create(Discipline discipline, UUID courseId, UUID organizationId)
            throws APIException, NotFoundException {
        validateFields(discipline, courseId);

        Course course = courseService.readById(courseId, organizationId);
        if (Objects.isNull(course))
            throw new NotFoundException("Not found course with id: " + courseId);

        discipline.setCourse(course);
        Discipline disciplineSaved = repository.save(discipline);

        return disciplineSaved.getId();
    }

    @Override
    public UUID update(Discipline discipline, UUID courseId, UUID organizationId) {
        validateFields(discipline, courseId);

        if (Objects.isNull(discipline.getId()))
            throw new NotFoundException("[Discipline id] id NULL");

        Discipline entity = repository.readByIdAndOrganizationId(discipline.getId(), organizationId);

        if (Objects.isNull(entity))
            throw new NotFoundException("Not found discipline with id: " + discipline.getId());

        if (!entity.getName().equals(discipline.getName())) {
            entity.setName(discipline.getName());
        }

        if (!entity.getCode().equals(discipline.getCode())) {
            entity.setCode(discipline.getCode());
        }

        if (!entity.getCourse().getId().equals(courseId)) {
            Course course = courseService.readById(courseId, organizationId);
            if (Objects.isNull(course))
                throw new NotFoundException("Not found course with id: " + courseId);

            entity.setCourse(course);
        }

        entity.setUpdatedAt(discipline.getUpdatedAt());
        entity.setUpdatedBy(discipline.getUpdatedBy());
        entity = repository.save(entity);

        return entity.getId();
    }

    @Override
    public Discipline readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException {
        if (Objects.isNull(id) || Objects.isNull(organizationId))
            throw new APIException("[discipline id] or [organization id] is NULL.");

        return repository.readByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) {
        if (Objects.isNull(id) || Objects.isNull(organizationId))
            throw new APIException("[discipline id] or [organization id] is NULL.");

        Discipline discipline = readByIdAndOrganizationId(id, organizationId);

        if (Objects.isNull(discipline))
            throw new NotFoundException("Not found discipline with id: " + id);

        discipline.setDeleted(Boolean.TRUE);
        discipline.setDeletedAt(ZonedDateTime.now());

        repository.save(discipline);

        return Boolean.TRUE;
    }

    @Override
    public List<Discipline> readAllByOrganizationId(UUID organizationId) throws APIException {
        if (Objects.isNull(organizationId))
            throw new APIException("[organization id] is NULL.");
        return repository.readAllByOrganizationId(organizationId);
    }

    @Override
    public List<Discipline> readAllByCourseId(UUID organizationId, UUID id) throws APIException {
        if (Objects.isNull(id) || Objects.isNull(organizationId))
            throw new APIException("[course id] or [organization id] is NULL.");

        return repository.findByOrganizationIdAndCourse_Id(organizationId, id);
    }

    @Override
    public List<Discipline> readAllByCourseIdAndTeacherIdAndOrganizationId(UUID courseId, UUID userId, UUID organizationId) {
        if (Objects.isNull(courseId) || Objects.isNull(organizationId) || Objects.isNull(userId))
            throw new APIException("[course id], [user id] or [organization id] is NULL.");

        return enrollmentTeacherCustomRepository.readAllDisciplinesByTeacher(courseId, userId, organizationId);
    }

    @Override
    public List<DTODisciplineScore> listBulletin(UUID courseId, UUID clazzId, UUID studentId, UUID organizationId) throws APIException {
        if (Objects.isNull(clazzId))
            throw new APIException("[id clazz] is null");

        if (Objects.isNull(courseId))
            throw new APIException("[id course] is null");

        if (Objects.isNull(studentId))
            throw new APIException("[id student] is null");

        if (Objects.isNull(organizationId))
            throw new APIException("[id organization] is null");

        return customRepository.listDisciplinesAndScores(courseId, clazzId, studentId, organizationId);
    }

    private void validateFields(Discipline discipline, UUID courseId) throws APIException {
        if (Objects.isNull(discipline))
            throw new APIException("[discipline] is null");

        if (Objects.isNull(courseId))
            throw new APIException("[id course] is null");
    }
}
