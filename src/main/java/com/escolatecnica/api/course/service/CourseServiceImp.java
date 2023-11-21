package com.escolatecnica.api.course.service;

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
public class CourseServiceImp implements CourseService{

    private final CourseRepository repository;

    private final EnrollmentTeacherCustomRepository enrollmentTeacherRepository;

    @Override
    public UUID create(Course element) throws APIException {
        if(Objects.isNull(element)) throw new APIException("Course is null.");
        if(Objects.isNull(element.getCode()) || Objects.isNull(element.getName())){
            throw new APIException("Course code or name is null.");
        }

        if(repository.existsByCodeAndNameAndOrganizationId(element.getCode(), element.getName(), element.getOrganizationId())){
            throw new APIException("Course already exists in [Organization]:"+element.getOrganizationId());
        }

        Course course = repository.save(element);

        return course.getId();
    }

    @Override
    public Course readById(UUID id, UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(id) || Objects.isNull(organizationId)){
            throw new APIException("[Course id] or [User id] is null.");
        }

        Optional<Course> optional = repository.readByIdAndOrganizationId(id, organizationId);
        return optional.orElseThrow(() -> new NotFoundException("Not found element with id: "+id));
    }

    @Override
    public List<Course> readAllByOrganization(UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(organizationId)){
            throw new APIException("[Organization id] is null.");
        }

        return repository.readAllByOrganizationId(organizationId);
    }

    @Override
    public UUID update(Course element) throws APIException, NotFoundException {
        if(Objects.isNull(element)) throw new APIException("Course is null.");

        if(Objects.isNull(element.getId()) || Objects.isNull(element.getCode()) || Objects.isNull(element.getName())){
            throw new APIException("Course code, name or id is null.");
        }

        if(repository.existsByCodeAndNameAndOrganizationId(element.getCode(), element.getName(), element.getOrganizationId())){
            throw new APIException("Course already exists in [Organization]:"+element.getOrganizationId());
        }

        Optional<Course> optional = repository.readByIdAndOrganizationId(element.getId(), element.getOrganizationId());
        if(optional.isEmpty()){
            throw new NotFoundException("Course from id ["+element.getId()+"] is not exist");
        }

        Course course = optional.get();

        course.setUpdatedAt(ZonedDateTime.now());
        course.setUpdatedBy(element.getUpdatedBy());
        course.setCode(element.getCode());
        course.setName(element.getName());

        course = repository.save(course);

        return course.getId();
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(id) || Objects.isNull(organizationId)){
            throw new APIException("[Organization id] or [Course id] is null.");
        }

        Optional<Course> optional = repository.readByIdAndOrganizationId(id, organizationId);

        if(optional.isEmpty()){
            throw new NotFoundException("Not found [Course] with the IDs informed.");
        }

        Course course = optional.get();
        course.setDeleted(Boolean.TRUE);
        course.setDeletedAt(ZonedDateTime.now());

        repository.save(course);

        return Boolean.TRUE;
    }

    @Override
    public List<Course> readAllByOrganizarionIdAndTeacherId(UUID userId, UUID organizationId) throws APIException {
        if(Objects.isNull(userId) || Objects.isNull(organizationId)){
            throw new APIException("[Organization id] or [User id] is null.");
        }
        return enrollmentTeacherRepository.readAllCoursesByTeacher(userId, organizationId);
    }
}
