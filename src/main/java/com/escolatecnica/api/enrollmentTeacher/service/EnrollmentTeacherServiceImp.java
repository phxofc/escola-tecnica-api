package com.escolatecnica.api.enrollmentTeacher.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.clazz.repository.ClazzRepository;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.repository.CourseRepository;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.discipline.repository.DisciplineRepository;
import com.escolatecnica.api.enrollmentTeacher.model.EnrollmentTeacher;
import com.escolatecnica.api.enrollmentTeacher.repository.EnrollmentTeacherRepository;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnrollmentTeacherServiceImp implements EnrollmentTeacherService {

    private final EnrollmentTeacherRepository repository;
    private final CourseRepository courseRepository;
    private final ClazzRepository clazzRepository;
    private final DisciplineRepository disciplineRepository;
    private final UserRepository userRepository;

    @Override
    public boolean create(UUID courseId, UUID clazzId, UUID disciplineId, UUID teacherId, UUID organizationId,
            UUID registryBy) throws APIException, NotFoundException {
        validateFields(courseId, clazzId, disciplineId, teacherId, organizationId, registryBy);

        Optional<Course> optionalCourse = courseRepository.readByIdAndOrganizationId(courseId, organizationId);
        if (optionalCourse.isEmpty())
            throw new NotFoundException("Not found course with id: " + courseId);

        Optional<User> optionalUser = userRepository.readByIdAndOrganizationId(teacherId, organizationId);
        if (optionalUser.isEmpty())
            throw new NotFoundException("Not found user with id: " + teacherId);

        Clazz clazz = clazzRepository.findByOrganizationIdAndId(organizationId, clazzId);
        if (Objects.isNull(clazz))
            throw new NotFoundException("Not found clazz with id: " + clazzId);

        Discipline discipline = disciplineRepository.readByIdAndOrganizationId(disciplineId, organizationId);
        if (Objects.isNull(discipline))
            throw new NotFoundException("Not found discipline with id: " + disciplineId);

        EnrollmentTeacher enrollmentTeacher = EnrollmentTeacher.builder()
                .clazz(clazz)
                .course(optionalCourse.get())
                .discipline(discipline)
                .user(optionalUser.get())
                .registeredBy(registryBy)
                .createdAt(ZonedDateTime.now())
                .organizationId(organizationId)
                .build();

        repository.save(enrollmentTeacher);

        return Boolean.TRUE;
    }

    @Override
    public List<EnrollmentTeacher> readAllByOrganizationId(UUID organizationId) throws APIException {
        if (Objects.isNull(organizationId))
            throw new APIException("[id organization] is null");

        return repository.findByOrganizationId(organizationId);
    }

    @Override
    public EnrollmentTeacher readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException {
        if (Objects.isNull(organizationId) || Objects.isNull(id))
            throw new APIException("[id organization] or [id] is null");

        return repository.findByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public EnrollmentTeacher readByCourseAndDisciplineAndClazzAndUserAndOrganizationId(UUID idCourse, UUID idDiscipline, UUID idClazz, UUID idTeacher, UUID organizationId) {
        validateFields(idCourse, idClazz, idDiscipline, idTeacher, organizationId, UUID.randomUUID());
        return repository.findByCourse_IdAndDiscipline_IdAndClazz_IdAndUser_IdAndOrganizationId(
                idCourse,
                idDiscipline,
                idClazz,
                idTeacher,
                organizationId
        );
    }

    private void validateFields(
            UUID courseId,
            UUID clazzId,
            UUID disciplineId,
            UUID teacherId,
            UUID organizationId,
            UUID registryBy) throws APIException {

        if (Objects.isNull(clazzId))
            throw new APIException("[id clazz] is null");

        if (Objects.isNull(courseId))
            throw new APIException("[id course] is null");

        if (Objects.isNull(disciplineId))
            throw new APIException("[id discipline] is null");

        if (Objects.isNull(teacherId))
            throw new APIException("[id teacher] is null");

        if (Objects.isNull(organizationId))
            throw new APIException("[id organization] is null");

        if (Objects.isNull(registryBy))
            throw new APIException("[id registryBy] is null");
    }

}
