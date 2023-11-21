package com.escolatecnica.api.organization.service;

import com.escolatecnica.api.organization.model.Organization;
import com.escolatecnica.api.organization.repository.OrganizationRepository;
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
public class OrganizationServiceImp implements OrganizationService {

    private final OrganizationRepository repository;

    @Override
    public boolean create(Organization element) throws APIException {
        if (Objects.isNull(element))
            throw new APIException("Organization is empty");

        if (alreadyExist(element.getName()))
            throw new APIException("An organization with this name already exists");

        repository.save(element);
        return true;
    }

    @Override
    public Organization readById(UUID id) throws NotFoundException {

        if (Objects.nonNull(id)) {
            Optional<Organization> optional = repository.findById(id);
            if (optional.isPresent())
                return optional.get();
        }

        throw new NotFoundException("Not found organization of id: " + id);
    }

    @Override
    public List<Organization> readAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Organization element) throws APIException, NotFoundException {
        if (Objects.isNull(element))
            throw new APIException("Organization is empty");

        if (alreadyExist(element.getName()))
            throw new APIException("An organization with this name already exists");

        Optional<Organization> optional = repository.findById(element.getId());

        if (optional.isEmpty())
            throw new NotFoundException("Could not find organization id: " + element.getId());

        if (Objects.nonNull(element.getName()) && !Objects.equals(element.getName(), optional.get().getName())) {
            optional.get().setCreatedAt(optional.get().getCreatedAt());
            optional.get().setRegisteredBy(optional.get().getRegisteredBy());
            optional.get().setName(element.getName());

            repository.save(optional.get());
            return true;
        } else {
            throw new APIException("Nothing Change in: " + element.getName());
        }

    }

    @Override
    public boolean delete(UUID id) throws NotFoundException, APIException {

        if (Objects.nonNull(id)) {
            Optional<Organization> optional = repository.findById(id);
            if (optional.isPresent()) {
                Organization org = optional.get();
                org.setDeletedAt(ZonedDateTime.now());
                org.setDeleted(Boolean.TRUE);
                repository.save(org);

                return true;
            }
        }

        throw new NotFoundException("Not found organization of id: " + id);
    }

    @Override
    public List<Organization> readByIsRootFalse() {
        return repository.findByIsRootFalse();
    }

    private boolean alreadyExist(String organizationName) {
        return repository.existsByName(organizationName);
    }

}
