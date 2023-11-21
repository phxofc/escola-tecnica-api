package com.escolatecnica.api.organization.controller;

import com.escolatecnica.api.organization.dto.DTOOrganization;
import com.escolatecnica.api.organization.model.Organization;
import com.escolatecnica.api.organization.service.OrganizationService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    private final JWTUtil jwtUtil;

    @GetMapping(value = "/login", produces = { "application/json" })
    public ResponseEntity<List<DTOOrganization>> readAll() {
        List<Organization> list = organizationService.readAll();
        List<DTOOrganization> dtoOrganizationList = new ArrayList<>();

        list.forEach(organization -> dtoOrganizationList
                .add(new DTOOrganization(organization.getId(), organization.getName())));

        return new ResponseEntity<>(dtoOrganizationList, HttpStatus.OK);
    }

    @GetMapping(produces = { "application/json" })
    public ResponseEntity<List<DTOOrganization>> readByIsRootFalse() {
        List<Organization> list = organizationService.readByIsRootFalse();
        List<DTOOrganization> dtoOrganizationList = new ArrayList<>();

        list.forEach(organization -> dtoOrganizationList
                .add(new DTOOrganization(organization.getId(), organization.getName())));

        return new ResponseEntity<>(dtoOrganizationList, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> create(HttpServletRequest request, @RequestBody DTOOrganization body) {
        boolean isCreation = Objects.isNull(body.id());

        UUID userId = jwtUtil.getUserIdFromRequest(request);
        Organization model = buildOrganization(body, isCreation, userId);

        try {
            boolean result;

            if (isCreation) {
                result = organizationService.create(model);
            } else {
                result = organizationService.update(model);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DTOOrganization> readById(@PathVariable UUID id) {
        try {
            Organization organization = organizationService.readById(id);
            return new ResponseEntity<>(new DTOOrganization(organization.getId(), organization.getName()),
                    HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        try {
            boolean result = organizationService.delete(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }
    }

    private Organization buildOrganization(DTOOrganization body, boolean isCreation, UUID userId) {
        Organization organization = new Organization();
        organization.setName(body.name());

        if (!isCreation) {
            organization.setId(body.id());
            organization.setUpdatedBy(userId);
            organization.setUpdatedAt(ZonedDateTime.now());
        } else {
            organization.setRegisteredBy(userId);
            organization.setCreatedAt(ZonedDateTime.now());
            organization.setIsRoot(Boolean.FALSE);
        }

        return organization;
    }

}
