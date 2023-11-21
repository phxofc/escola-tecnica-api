package com.escolatecnica.api.root.service;

import com.escolatecnica.api.root.model.BaseModel;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;

import java.util.List;
import java.util.UUID;
public interface CrudService<T extends BaseModel> {
    UUID create(T element) throws APIException;
    T readById(UUID id, UUID organizationId) throws APIException, NotFoundException;
    List<T> readAllByOrganization(UUID organizationId) throws APIException, NotFoundException;
    UUID update(T element) throws APIException, NotFoundException;
    boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException;
}
