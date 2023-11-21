package com.escolatecnica.api.enrollment.service;

import com.escolatecnica.api.enrollment.controller.EnrollmentController;
import com.escolatecnica.api.enrollment.model.Enrollment;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    boolean create(UUID organizationId, UUID courseId, UUID clazzId, User user, MultipartFile [] files) throws APIException, NotFoundException;

    List<Enrollment> readByOrganizationIdAndUserId(UUID organizationId, UUID id) throws APIException;

    boolean updateAccess(UUID studentId, UUID courseId, UUID clazzId, UUID organizationId) throws APIException, NotFoundException;
}
