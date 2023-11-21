package com.escolatecnica.api.discipline.controller;

import com.escolatecnica.api.discipline.dto.DTODiscipline;
import com.escolatecnica.api.discipline.dto.DTODisciplineList;
import com.escolatecnica.api.discipline.dto.DTODisciplineScore;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.discipline.service.DisciplineService;
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
@RequestMapping("/discipline")
public class DisciplineController {

    private final JWTUtil jwtUtil;

    private final DisciplineService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<UUID> create(HttpServletRequest request, @RequestBody DTODiscipline body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Discipline discipline = buildCourseToSave(body, orgId, userId);
            UUID result;

            if (Objects.isNull(discipline.getId())) {
                result = service.create(discipline, body.courseId(), orgId);
            } else {
                result = service.update(discipline, body.courseId(), orgId);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<DTODisciplineList>> readAll(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            List<Discipline> disciplines = service.readAllByOrganizationId(orgId);

            return new ResponseEntity<>(buildListOfDTO(disciplines), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DTODiscipline> readById(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Discipline discipline = service.readByIdAndOrganizationId(id, orgId);

            return new ResponseEntity<>(
                    new DTODiscipline(
                            discipline.getId(),
                            discipline.getCode(),
                            discipline.getName(),
                            discipline.getCourse().getId()),
                    HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/by-course/{courseId}", produces = "application/json")
    public ResponseEntity<List<DTODisciplineList>> readAllByCourseId(HttpServletRequest request, @PathVariable UUID courseId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            List<Discipline> disciplines = service.readAllByCourseId(orgId, courseId);

            return new ResponseEntity<>(buildListOfDTO(disciplines), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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


    @GetMapping(value = "/by-teacher/{courseId}", produces = "application/json")
    public ResponseEntity<List<DTODisciplineList>> readAllByTeacher(HttpServletRequest request, @PathVariable UUID courseId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            List<Discipline> disciplines = service.readAllByCourseIdAndTeacherIdAndOrganizationId(courseId, userId, orgId);

            return new ResponseEntity<>(buildListOfDTO(disciplines), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/bulletin", produces = "application/json")
    public ResponseEntity<?> listBulletin(
            HttpServletRequest request,
            @RequestParam UUID courseId,
            @RequestParam UUID clazzId
    ) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            List<DTODisciplineScore> data = service.listBulletin(courseId, clazzId, userId, orgId);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    private Discipline buildCourseToSave(DTODiscipline discipline, UUID organizationId, UUID userId)
            throws NotFoundException {

        if (Objects.isNull(discipline.id())) {
            return Discipline.builder()
                    .name(discipline.name())
                    .code(discipline.code())
                    .organizationId(organizationId)
                    .createdAt(ZonedDateTime.now())
                    .registeredBy(userId)
                    .build();
        }

        return Discipline.builder()
                .id(discipline.id())
                .name(discipline.name())
                .code(discipline.code())
                .organizationId(organizationId)
                .updatedAt(ZonedDateTime.now())
                .updatedBy(userId)
                .build();
    }


    private List<DTODisciplineList> buildListOfDTO(List<Discipline> list){
        List<DTODisciplineList> disciplineLists = new ArrayList<>();

        list.forEach(discipline -> disciplineLists.add(new DTODisciplineList(discipline.getId(),
                discipline.getCode(), discipline.getName(), discipline.getCourse().getName())));

        return disciplineLists;
    }

}
