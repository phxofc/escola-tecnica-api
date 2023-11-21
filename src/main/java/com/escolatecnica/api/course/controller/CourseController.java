package com.escolatecnica.api.course.controller;

import com.escolatecnica.api.course.dto.DTOCourse;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.service.CourseService;
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
@RequestMapping("/course")
public class CourseController {

    private final JWTUtil jwtUtil;
    private final CourseService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<UUID> create(HttpServletRequest request, @RequestBody DTOCourse body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Course course = buildCourseToSave(body, orgId, userId);
            UUID result;

            if (Objects.isNull(course.getId())) {
                result = service.create(course);
            } else {
                result = service.update(course);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<DTOCourse>> readAll(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            return getListResponseEntity(orgId);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<DTOCourse> readById(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Course course = service.readById(id, orgId);

            return new ResponseEntity<>(
                    new DTOCourse(course.getId(), course.getCode(), course.getName()),
                    HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            service.delete(id, orgId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/by-organization/{organizationId}", produces = "application/json")
    public ResponseEntity<List<DTOCourse>> readAllByOrganization(@PathVariable UUID organizationId) {
        try {
            return getListResponseEntity(organizationId);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/by-teacher/{teacherId}", produces = "application/json")
    public ResponseEntity<List<DTOCourse>> readAllByTeacher(HttpServletRequest request, @PathVariable UUID teacherId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            List<Course> courses = service.readAllByOrganizarionIdAndTeacherId(teacherId, orgId);
            List<DTOCourse> responseList = new ArrayList<>();

            courses.forEach(item -> responseList
                    .add(new DTOCourse(item.getId(), item.getCode(), item.getName())));

            return new ResponseEntity<>(responseList, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<List<DTOCourse>> getListResponseEntity(UUID organizationId) {
        List<Course> list = service.readAllByOrganization(organizationId);
        List<DTOCourse> responseList = new ArrayList<>();

        list.forEach(item -> responseList
                .add(new DTOCourse(item.getId(), item.getCode(), item.getName())));

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    private Course buildCourseToSave(DTOCourse course, UUID organizationId, UUID userId) {
        if (Objects.isNull(course.id())) {
            return Course.builder()
                    .name(course.name())
                    .code(course.code())
                    .organizationId(organizationId)
                    .createdAt(ZonedDateTime.now())
                    .registeredBy(userId)
                    .build();
        } else {
            return Course.builder()
                    .id(course.id())
                    .name(course.name())
                    .code(course.code())
                    .organizationId(organizationId)
                    .updatedAt(ZonedDateTime.now())
                    .updatedBy(userId)
                    .build();
        }
    }
}
