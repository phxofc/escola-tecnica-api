package com.escolatecnica.api.course.service;

import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.root.service.CrudService;
import com.escolatecnica.api.root.utils.APIException;

import java.util.List;
import java.util.UUID;

public interface CourseService extends CrudService<Course> {

    List<Course> readAllByOrganizarionIdAndTeacherId(UUID userId, UUID organizationId) throws APIException;

}
