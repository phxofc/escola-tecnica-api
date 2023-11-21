package com.escolatecnica.api.user.service;

import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.repository.CourseRepository;
import com.escolatecnica.api.documentation.service.DocumentationService;
import com.escolatecnica.api.enrollment.model.Enrollment;
import com.escolatecnica.api.enrollment.repository.EnrollmentRepository;
import com.escolatecnica.api.root.service.CrudService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.Role;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService, CrudService<User> {
    private final PasswordEncoder encoder;

    private final UserRepository repository;

    private final CourseRepository courseRepository;

    private final DocumentationService documentationService;

    private final EnrollmentRepository enrollmentRepository;


    @Override
    public boolean existsByCpfAndOrganizationId(String cpf, UUID organizationId) {
        return repository.existsByCpfAndOrganizationId(cpf, organizationId);
    }

    @Override
    public List<User> readAll() {
        return repository.findAll();
    }

    @Override
    public User readById(UUID id) throws NotFoundException, APIException {
        if(Objects.isNull(id)) throw new APIException("User id is null");

        Optional<User> optional = repository.findById(id);

        if(optional.isEmpty()) throw new NotFoundException("Not found user with id: "+id);

        return optional.get();
    }

    @Override
    public boolean delete(UUID id) throws NotFoundException, APIException {
        if(Objects.isNull(id)) throw new APIException("User id is null");

        Optional<User> optional = repository.findById(id);

        if(optional.isEmpty()) throw new NotFoundException("Not found user with id: "+id);

        optional.get().setDeleted(true);
        optional.get().setDeletedAt(ZonedDateTime.now());

        repository.save(optional.get());

        return Boolean.TRUE;
    }

    @Override
    public List<User> readByOrganizationIdAndRole(UUID organizationId, Role role) throws APIException {
        if(Objects.isNull(organizationId) || Objects.isNull(role)) throw new APIException("[organizationId] or [role] is null.");

        return repository.readByOrganizationIdAndRole(organizationId, role);
    }

    @Override
    public boolean updateAccess(UUID id, UUID organizationId) throws APIException, NotFoundException{
        if(Objects.isNull(id) || Objects.isNull(organizationId)) throw new APIException("[organizationId] or [id] is null.");

        Optional<User> optional = repository.readByIdAndOrganizationId(id, organizationId);

        if(optional.isEmpty()) throw new NotFoundException("User of "+id+ " not found.");

        User user = optional.get();

        user.setEnabledAccess(!user.getEnabledAccess());
        user.setUpdatedAt(ZonedDateTime.now());

        repository.save(user);
        return Boolean.TRUE;
    }


    @Override
    public UUID create(User element) throws APIException {
        if(Objects.isNull(element)) throw new APIException("User is null.");
        if(fieldsNonValid(element)) throw new APIException("One of the fields is null.");

        if(existsByCpfAndOrganizationId(element.getCpf(), element.getOrganizationId())){
            throw new APIException("User already exists in this organization");
        }

        element.setCreatedAt(ZonedDateTime.now());
        element.setPassword(encoder.encode(element.getPassword()));

        User user = repository.save(element);
        return user.getId();
    }

    @Override
    public User readById(UUID id, UUID organizationId) throws APIException, NotFoundException{
        if(Objects.isNull(id) || Objects.isNull(organizationId)){
            throw new APIException("[Organization id] or [User id] is null.");
        }

        Optional<User> optional = repository.readByIdAndOrganizationId(id, organizationId);
        return optional.orElseThrow(() -> new NotFoundException("Not found element with id: "+id));
    }

    @Override
    public List<User> readAllByOrganization(UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(organizationId)){
            throw new APIException("[Organization id] is null.");
        }

        return repository.readAllByOrganizationId(organizationId);
    }

    @Override
    public UUID update(User element) throws APIException, NotFoundException {
        if(Objects.isNull(element)) throw new APIException("User is null.");
        if(fieldsNonValid(element)) throw new APIException("One of the fields is null");

        Optional<User> optional = repository.findById(element.getId());
        if(optional.isEmpty()){
            throw new NotFoundException("User from id ["+element.getId()+"] is not exist");
        }

        User user = optional.get();

        user.setUpdatedAt(ZonedDateTime.now());
        user.setUpdatedBy(element.getUpdatedBy());
        user.setName(element.getName());
        user.setLastName(element.getLastName());
        user.setEmail(element.getEmail());
        user.setCpf(element.getCpf());
        user.setEnabledAccess(element.getEnabledAccess());
        user.setOrganizationId(element.getOrganizationId());

        user = repository.save(user);
        return user.getId();
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(id) || Objects.isNull(organizationId)){
            throw new APIException("[Organization id] or [User id] is null.");
        }

        Optional<User> optional = repository.readByIdAndOrganizationId(id, organizationId);

        if(optional.isEmpty()){
            throw new NotFoundException("Not found [User] with the IDs informed");
        }

        User user = optional.get();
        user.setDeleted(Boolean.TRUE);
        user.setDeletedAt(ZonedDateTime.now());

        repository.save(user);

        return true;
    }

    private boolean fieldsNonValid(User user){
        boolean isCreation = Objects.isNull(user.getId());

        boolean baseValid =
                Objects.nonNull(user.getName()) &&
                Objects.nonNull(user.getLastName()) &&
                Objects.nonNull(user.getEmail()) &&
                Objects.nonNull(user.getCpf()) &&
                Objects.nonNull(user.getRole()) &&
                Objects.nonNull(user.getOrganizationId());

        if(isCreation){
            return !baseValid || !Objects.nonNull(user.getPassword());
        }

        return !baseValid;
    }

    private Enrollment buildStudentCourse(User user, Course course, UUID organizationId){
        return Enrollment.builder()
                .organizationId(organizationId)
                .user(user)
                .course(course)
                .createdAt(ZonedDateTime.now())
                .registrationApproved(Boolean.FALSE)
                .build();
    }


}
