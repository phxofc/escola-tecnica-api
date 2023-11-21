package com.escolatecnica.api.clazz.controller;

import com.escolatecnica.api.clazz.dto.ClazzRequest;
import com.escolatecnica.api.clazz.dto.ClazzResponse;
import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.clazz.service.ClazzService;
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
@RequestMapping("/clazz")
public class ClazzController {

    private final JWTUtil jwtUtil;
    private final ClazzService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> create(HttpServletRequest request, @RequestBody ClazzRequest body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Clazz clazz = buildClazzToSave(body, orgId, userId);
            boolean result;

            if (Objects.isNull(clazz.getId())) {
                result = service.create(clazz, body.courseId(), orgId);
            } else {
                result = service.update(clazz, body.courseId(), orgId);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ClazzResponse>> readAll(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            List<Clazz> clazzes = service.readAllByOrganizationId(orgId);
            List<ClazzResponse> clazzResponses = new ArrayList<>();

            clazzes.forEach(clazz -> clazzResponses.add(new ClazzResponse(
                    clazz.getId(), clazz.getCode(), clazz.getIsConsolidated(),
                    clazz.getCourse().getId(), clazz.getCourse().toString())));

            return new ResponseEntity<>(clazzResponses, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ClazzResponse> readById(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Clazz clazz = service.readByOrganizationIdAndId(orgId, id);

            return new ResponseEntity<>(
                    new ClazzResponse(
                            clazz.getId(), clazz.getCode(), clazz.getIsConsolidated(),
                            clazz.getCourse().getId(), clazz.getCourse().toString()),
                    HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            boolean result = service.detele(orgId, id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/open/{courseId}", produces = "application/json")
    public ResponseEntity<List<?>> readAllOpenClazzs(HttpServletRequest request, @PathVariable UUID courseId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            List<Clazz> clazzes = service.readByNonConsolidation(orgId, courseId);

            return new ResponseEntity<>(clazzListToDTOList(clazzes), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/login/{courseId}/{orgId}", produces = "application/json")
    public ResponseEntity<List<?>> readAllOpenClazzsToLogin(@PathVariable UUID courseId, @PathVariable UUID orgId) {
        try {
            List<Clazz> clazzes = service.readByNonConsolidation(orgId, courseId);

            return new ResponseEntity<>(clazzListToDTOList(clazzes), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/manage-consolidation/{id}", produces = "application/json")
    public ResponseEntity<Boolean> manageConsolidation(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            boolean result = service.manageConsolidation(orgId, id);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/material/{courseId}/{disciplineId}", produces = "application/json")
    public ResponseEntity<List<?>> getClazzsByTeacher(
            HttpServletRequest request,
            @PathVariable UUID courseId,
            @PathVariable UUID disciplineId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            List<Clazz> clazzes = service.readAllClazzsByTeacher(courseId, disciplineId, userId, orgId);

            return new ResponseEntity<>(clazzListToDTOList(clazzes), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<?> clazzListToDTOList(List<Clazz> clazzes) {
        record ClazzResponse(UUID id, String code) {
        }

        List<ClazzResponse> clazzResponses = new ArrayList<>();
        clazzes.forEach(clazz -> clazzResponses.add(new ClazzResponse(clazz.getId(), clazz.getCode())));

        return clazzResponses;
    }

    private Clazz buildClazzToSave(ClazzRequest body, UUID organizationId, UUID userId) {
        if (Objects.isNull(body.id())) {
            return Clazz.builder()
                    .code(body.code())
                    .organizationId(organizationId)
                    .createdAt(ZonedDateTime.now())
                    .registeredBy(userId)
                    .build();
        } else {
            return Clazz.builder()
                    .id(body.id())
                    .code(body.code())
                    .organizationId(organizationId)
                    .updatedAt(ZonedDateTime.now())
                    .updatedBy(userId)
                    .build();
        }
    }
}
