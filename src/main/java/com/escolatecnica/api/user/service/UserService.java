package com.escolatecnica.api.user.service;

import com.escolatecnica.api.root.service.CrudService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.Role;
import com.escolatecnica.api.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService extends CrudService<User> {

    boolean existsByCpfAndOrganizationId(String cpf, UUID organizationId);

    List<User> readAll();

    User readById(UUID id) throws NotFoundException, APIException;

    boolean delete(UUID id) throws NotFoundException, APIException;

    List<User> readByOrganizationIdAndRole(UUID organizationId, Role role) throws APIException;

    boolean updateAccess(UUID id, UUID organizationId) throws NotFoundException, APIException;

}
