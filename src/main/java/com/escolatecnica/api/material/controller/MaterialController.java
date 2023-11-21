package com.escolatecnica.api.material.controller;

import com.escolatecnica.api.answer.service.AnswerService;
import com.escolatecnica.api.material.dto.DTOMaterialRequest;
import com.escolatecnica.api.material.dto.DTOMaterialResponse;
import com.escolatecnica.api.material.dto.DTOMaterialStudent;
import com.escolatecnica.api.material.dto.DTOMaterialToListTeacher;
import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.material.model.Type;
import com.escolatecnica.api.material.service.MaterialService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService service;
    private final AnswerService answerService;
    private final JWTUtil jwtUtil;

    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Boolean> create(HttpServletRequest request, @ModelAttribute DTOMaterialRequest body) {
        try {
            UUID organizationId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Material material = Material.builder()
                    .title(body.title())
                    .description(body.description())
                    .type(Type.valueOf(body.type()))
                    .registeredBy(userId)
                    .build();

            Boolean result = service.create(
                    material,
                    body.file(),
                    organizationId,
                    body.clazzId(),
                    body.courseId(),
                    body.disciplineId());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/by-teacher", produces = "application/json")
    public ResponseEntity<List<DTOMaterialToListTeacher>> readAllByTeacher(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID teacherId = jwtUtil.getUserIdFromRequest(request);

            List<DTOMaterialToListTeacher> materialList = new ArrayList<>();
            List<Material> materials = service.readAllByTeacher(orgId, teacherId);

            materials.forEach(material -> materialList.add(
                    new DTOMaterialToListTeacher(
                            material.getId(),
                            material.getTitle(),
                            material.getDescription(),
                            material.getType(),
                            material.getType().getDescription(),
                            material.getIsActive(),
                            material.getAttachedFileName(),
                            material.getCreatedAt(),
                            material.getCourse().toString(),
                            material.getClazz().getCode(),
                            material.getDiscipline().toString())));

            return new ResponseEntity<>(materialList, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DTOMaterialResponse> readById(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Material material = service.readByIdAndOrganizationId(id, orgId);

            return new ResponseEntity<>(
                    new DTOMaterialResponse(
                            material.getId(),
                            material.getTitle(),
                            material.getDescription(),
                            material.getType(),
                            material.getAttachedFileName()),
                    HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/discipline", produces = "application/json")
    public ResponseEntity<List<DTOMaterialStudent>> readAllForStudent(
            HttpServletRequest request,
            @RequestParam UUID courseId,
            @RequestParam UUID clazzId,
            @RequestParam UUID disciplineId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            List<Material> list = service.readAllByOrganizationAndCourseAndClazzAndDiscipline(orgId, courseId, clazzId,
                    disciplineId);

            List<DTOMaterialStudent> response = new ArrayList<>();

            list.forEach(it -> response.add(new DTOMaterialStudent(
                    it.getId(),
                    it.getTitle(),
                    it.getDescription(),
                    it.getType().getDescription(),
                    it.getIsActive(),
                    it.getAttachedFileName(),
                    it.getCreatedAt(),
                    it.getType(),
                    answerService.thereIsAnswerAssociatedWithMaterial(userId, it.getId(), orgId))));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/manage-submission/{materialId}", produces = "application/json")
    public ResponseEntity<Boolean> manageTaskSubmission(HttpServletRequest request, @PathVariable UUID materialId) {
        try {
            UUID organizationId = jwtUtil.getOrganizationIdFromRequest(request);
            return new ResponseEntity<>(service.manageTaskSubmission(materialId, organizationId), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Boolean> delete(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            boolean result = service.delete(id, orgId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }
    }

}
