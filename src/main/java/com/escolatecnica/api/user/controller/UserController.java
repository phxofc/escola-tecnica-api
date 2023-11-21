package com.escolatecnica.api.user.controller;

import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.dto.DTOStudent;
import com.escolatecnica.api.user.dto.DTOUserRequest;
import com.escolatecnica.api.user.dto.DTOUser;
import com.escolatecnica.api.user.model.Role;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;
    private final JWTUtil jwtUtil;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<UUID> create(HttpServletRequest request, @RequestBody DTOUserRequest body) {
        try {
            User user = buildUserToSave(body, request);
            UUID result;

            if (Objects.isNull(user.getId())) {
                result = service.create(user);
            } else {
                result = service.update(user);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<DTOUser>> readAll(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            String roleString = jwtUtil.getRoleFromRequest(request);

            List<User> list;
            if (isAdmin(roleString))
                list = service.readAll();
            else
                list = service.readAllByOrganization(orgId);

            List<DTOUser> responseList = new ArrayList<>();

            list.forEach(item -> responseList.add(buildUserResponse(item)));

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DTOUser> readById(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            String roleString = jwtUtil.getRoleFromRequest(request);

            User user;
            if (isAdmin(roleString))
                user = service.readById(id);
            else
                user = service.readById(id, orgId);

            return new ResponseEntity<>(buildUserResponse(user), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(HttpServletRequest request, @PathVariable UUID id) {
        try {
            String roleString = jwtUtil.getRoleFromRequest(request);
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            if (isAdmin(roleString))
                service.delete(id);
            else
                service.delete(id, orgId);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/by-role/{roleName}", produces = "application/json")
    public ResponseEntity<List<DTOUser>> readAllByRole(HttpServletRequest request, @PathVariable String roleName) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Role role = Role.valueOf(roleName);

            List<User> list = service.readByOrganizationIdAndRole(orgId, role);
            List<DTOUser> responseList = new ArrayList<>();

            list.forEach(item -> responseList.add(buildUserResponse(item)));

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    record DTOUpdateAccessUser(UUID id, UUID organizationId) {
    }

    @PostMapping(value = "/update-access", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> updateAccess(@RequestBody DTOUpdateAccessUser body) {
        try {
            Boolean result = service.updateAccess(body.id(), body.organizationId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/students", produces = "application/json")
    public ResponseEntity<List<DTOStudent>> studentsWithDocumentsUnderReview(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            List<User> list = service.readByOrganizationIdAndRole(orgId, Role.STUDENT);

            List<DTOStudent> responseList = new ArrayList<>();
            list.forEach(user -> responseList.add(buildStudentResponse(user)));

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (NotFoundException | IllegalArgumentException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private User buildUserToSave(DTOUserRequest dtoUserRequest, HttpServletRequest request) {
        UUID userLoggedId = jwtUtil.getUserIdFromRequest(request);
        UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
        String roleStr = jwtUtil.getRoleFromRequest(request);

        User user = User.builder()
                .organizationId(isAdmin(roleStr) ? dtoUserRequest.organizationId() : orgId)
                .id(dtoUserRequest.id())
                .cpf(dtoUserRequest.cpf())
                .email(dtoUserRequest.email())
                .lastName(dtoUserRequest.lastName())
                .name(dtoUserRequest.name())
                .role(dtoUserRequest.role())
                .password(dtoUserRequest.password())
                .enabledAccess(dtoUserRequest.enabledAccess())
                .build();

        if (Objects.isNull(dtoUserRequest.id())) {
            user.setRegisteredBy(userLoggedId);
        } else {
            user.setUpdatedBy(userLoggedId);
        }

        return user;
    }

    private DTOStudent buildStudentResponse(User user) {
        return new DTOStudent(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getCpf(),
                user.getEmail(),
                user.getDocumentation().getCpfFileName(),
                user.getDocumentation().getRgFileName(),
                user.getDocumentation().getCrFileName(),
                user.getEnabledAccess());
    }

    private DTOUser buildUserResponse(User user) {
        return new DTOUser(
                user.getId(),
                user.getOrganizationId(),
                user.getName(),
                user.getLastName(),
                user.getCpf(),
                user.getEmail(),
                user.getRole(),
                user.getEnabledAccess(),
                user.getRole().getDescription());
    }

    private boolean isAdmin(String roleStr) throws IllegalArgumentException {
        try {
            return Objects.nonNull(roleStr) && Role.valueOf(roleStr).equals(Role.ADMIN);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Role name is invalid");
        }
    }

}
