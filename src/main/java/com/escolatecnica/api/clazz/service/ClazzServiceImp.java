package com.escolatecnica.api.clazz.service;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.clazz.repository.ClazzRepository;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.repository.CourseRepository;
import com.escolatecnica.api.enrollmentTeacher.repository.EnrollmentTeacherCustomRepository;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ClazzServiceImp implements ClazzService {

    private final ClazzRepository repository;

    private final CourseRepository courseRepository;

    private final EnrollmentTeacherCustomRepository teacherCustomRepository;

    @Override
    public boolean create(Clazz clazz, UUID courseId, UUID organizationId) throws APIException, NotFoundException {
        if (Objects.isNull(clazz)) throw new APIException("[course] is null.");

        if (Objects.isNull(organizationId) || Objects.isNull(courseId)) throw new APIException("[organization id] or [course id] is null.");

        if(existsClazz(organizationId, clazz.getCode(), courseId)) throw new APIException("This course already exists.");

        Optional<Course> optional = courseRepository.readByIdAndOrganizationId(courseId, organizationId);

        if (optional.isEmpty()) throw new NotFoundException("Not found [course] with id: " + courseId);

        clazz.setCourse(optional.get());
        clazz.setIsConsolidated(Boolean.FALSE);
        clazz.setCreatedAt(ZonedDateTime.now());

        repository.save(clazz);

        return Boolean.TRUE;
    }

    @Override
    public boolean update(Clazz clazz, UUID courseId, UUID organizationId) throws APIException, NotFoundException {
        if (Objects.isNull(clazz)) throw new APIException("[course] is null.");

        if (Objects.isNull(organizationId) || Objects.isNull(courseId)) throw new APIException("[organization id] or [course id] is null.");

        UUID id = clazz.getId();
        Clazz clazzSaved = repository.findByOrganizationIdAndId(organizationId, id);

        if(existsClazz(organizationId, clazz.getCode(), courseId)){
            if(!(clazz.getCode().equals(clazzSaved.getCode()) && clazzSaved.getCourse().getId().equals(courseId))){
                throw new APIException("This course already exists.");
            }
        }

        if (!clazzSaved.getCode().equals(clazz.getCode())) {
            clazzSaved.setCode(clazz.getCode());
        }

        if (!clazzSaved.getCourse().getId().equals(courseId)) {
            Optional<Course> optional = courseRepository.readByIdAndOrganizationId(courseId, organizationId);
            if (optional.isEmpty()) throw new NotFoundException("Not found [course] with id: " + courseId);

            clazzSaved.setCourse(optional.get());
        }

        clazzSaved.setUpdatedAt(ZonedDateTime.now());
        repository.save(clazzSaved);

        return Boolean.TRUE;
    }

    @Override
    public boolean detele(UUID organizationId, UUID clazzId) throws APIException, NotFoundException {
        if (Objects.isNull(organizationId) || Objects.isNull(clazzId))
            throw new APIException("[organization id] or [clazz id] is null.");

        Clazz clazzSaved = repository.findByOrganizationIdAndId(organizationId, clazzId);

        if (Objects.isNull(clazzSaved))
            throw new NotFoundException("Not found clazz with id: " + clazzId);

        clazzSaved.setDeletedAt(ZonedDateTime.now());
        clazzSaved.setDeleted(Boolean.TRUE);

        return Boolean.TRUE;
    }

    @Override
    public List<Clazz> readByNonConsolidation(UUID organizationId, UUID id) throws APIException {
        if (Objects.isNull(organizationId) || Objects.isNull(id))
            throw new APIException("[organization id] or [course id] is null.");
        return repository.findByOrganizationIdAndIsConsolidatedFalseAndCourse_Id(organizationId, id);
    }

    @Override
    public List<Clazz> readAllByOrganizationId(UUID organizationId) throws APIException {
        if (Objects.isNull(organizationId))
            throw new APIException("[organization id] is null.");
        return repository.findByOrganizationId(organizationId);
    }

    @Override
    public Clazz readByOrganizationIdAndId(UUID organizationId, UUID id) throws APIException {
        if (Objects.isNull(organizationId) || Objects.isNull(id))
            throw new APIException("[organization id] or [id] is null.");
        return repository.findByOrganizationIdAndId(organizationId, id);
    }

    @Override
    public boolean manageConsolidation(UUID organizationId, UUID clazzId) throws APIException, NotFoundException {
        if (Objects.isNull(organizationId) || Objects.isNull(clazzId))
            throw new APIException("[organization id] or [clazz id] is null.");

        Clazz clazzSaved = repository.findByOrganizationIdAndId(organizationId, clazzId);

        if (Objects.isNull(clazzSaved))
            throw new NotFoundException("Not found clazz with id: " + clazzId);

        clazzSaved.setIsConsolidated(!clazzSaved.getIsConsolidated());

        repository.save(clazzSaved);

        return Boolean.TRUE;
    }

    @Override
    public List<Clazz> readAllClazzsByTeacher(UUID courseId, UUID disciplineId, UUID userId, UUID organizationId)
            throws APIException {
        if (Objects.isNull(courseId) || Objects.isNull(disciplineId) || Objects.isNull(userId)
                || Objects.isNull(organizationId))
            throw new APIException("[organization], [discipline], [user] or [organization] {id} is null.");

        return teacherCustomRepository.readAllClazzsByTeacher(courseId, disciplineId, userId, organizationId);
    }

    @Override
    public boolean existsClazz(UUID organizationId, String code, UUID id) {
        return repository.existsByOrganizationIdAndCodeAndCourse_Id(organizationId, code, id);
    }

}
