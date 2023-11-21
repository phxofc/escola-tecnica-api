package com.escolatecnica.api.material.service;

import com.escolatecnica.api.material.dto.DTOMaterialStudent;
import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MaterialService {

    boolean create(Material material, MultipartFile multipartFile, UUID organizationId, UUID clazzId, UUID courseId, UUID disciplineId) throws NotFoundException, APIException;

    List<Material> readAllByTeacher(UUID organizationId, UUID teacherId) throws APIException;

    Material readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException;

    List<Material> readAllByOrganizationAndCourseAndClazzAndDiscipline(UUID organizationId, UUID courseId, UUID clazzId, UUID disciplineId) throws APIException;

    boolean manageTaskSubmission(UUID id, UUID organizationId) throws APIException, NotFoundException;

    boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException;

}
