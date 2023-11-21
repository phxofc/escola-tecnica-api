package com.escolatecnica.api.organization.service;

import com.escolatecnica.api.organization.model.Organization;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    boolean create(Organization element) throws APIException;
    Organization readById(UUID id) throws NotFoundException;
    List<Organization> readAll();
    boolean update(Organization element) throws APIException, NotFoundException;
    boolean delete(UUID id) throws NotFoundException, APIException;

    List<Organization> readByIsRootFalse();

}
