package com.escolatecnica.api.enrollment.service;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.clazz.repository.ClazzRepository;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.service.CourseService;
import com.escolatecnica.api.documentation.model.Documentation;
import com.escolatecnica.api.documentation.service.DocumentationService;
import com.escolatecnica.api.enrollment.model.Enrollment;
import com.escolatecnica.api.enrollment.repository.EnrollmentRepository;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EnrollmentServiceImp implements EnrollmentService {

    private final ClazzRepository clazzRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final DocumentationService documentationService;
    private final EnrollmentRepository repository;


    @Override
    public boolean create(UUID organizationId, UUID courseId, UUID clazzId, User user, MultipartFile[] files)
            throws APIException, NotFoundException {
        if (Objects.isNull(user))
            throw new APIException("User is null.");

        if (Objects.isNull(organizationId) || Objects.isNull(courseId) || Objects.isNull(clazzId)) {
            throw new APIException("[organizationId] or [clazzId] or [courseId] is null.");
        }

        Documentation documentationSaved = documentationService.createDocumetation(files, organizationId);

        if (Objects.isNull(documentationSaved)) {
            throw new APIException("[documentation] is null.");
        }

        user.setDocumentation(documentationSaved);

        UUID userId = userService.create(user);
        Course course = courseService.readById(courseId, organizationId);
        Clazz clazz = clazzRepository.findByOrganizationIdAndId(organizationId, clazzId);

        if (Objects.isNull(userId) || Objects.isNull(course) || Objects.isNull(clazz)) {
            throw new APIException("[userId], [clazz] [course] is null.");
        }

        user.setId(userId);

        Enrollment registration = Enrollment.builder()
                .organizationId(organizationId)
                .user(user)
                .course(course)
                .clazz(clazz)
                .createdAt(ZonedDateTime.now())
                .registrationApproved(Boolean.FALSE)
                .registeredBy(userId)
                .build();

        repository.save(registration);

        return Boolean.TRUE;
    }

    @Override
    public List<Enrollment> readByOrganizationIdAndUserId(UUID organizationId, UUID userId) throws APIException {
        if (Objects.isNull(organizationId) || Objects.isNull(userId))
            throw new APIException("[organizationId] or [userId] is null.");
        return repository.readByOrganizationIdAndUserId(organizationId, userId);
    }

    @Override
    public boolean updateAccess(UUID studentId, UUID courseId, UUID clazzId, UUID organizationId) throws APIException, NotFoundException {
        if (Objects.isNull(organizationId) || Objects.isNull(courseId) || Objects.isNull(clazzId) || Objects.isNull(studentId)) {
            throw new APIException("[organizationId], [clazzId], [studentId] or [courseId] is null.");
        }

        Enrollment enrollment = repository.findByCourseAndClazzAndStudentAndOrganization(organizationId, courseId, clazzId, studentId);
        enrollment.setRegistrationApproved(Boolean.TRUE);
        enrollment.setUpdatedAt(ZonedDateTime.now());

        repository.save(enrollment);

        return Boolean.TRUE;
    }

}
