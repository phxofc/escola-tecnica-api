package com.escolatecnica.api.enrollmentTeacher.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escolatecnica.api.enrollmentTeacher.dto.DTORequest;
import com.escolatecnica.api.enrollmentTeacher.dto.DTOResponse;
import com.escolatecnica.api.enrollmentTeacher.model.EnrollmentTeacher;
import com.escolatecnica.api.enrollmentTeacher.service.EnrollmentTeacherService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/enrollment-teacher")
public class EnrollmentTeacherController {

    private final JWTUtil jwtUtil;
    private final EnrollmentTeacherService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> create(HttpServletRequest request, @RequestBody DTORequest body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            boolean result = service.create(body.courseId(), body.clazzId(), body.disciplineId(), body.teacherId(),
                    orgId, userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<DTOResponse>> readAll(HttpServletRequest request) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            List<EnrollmentTeacher> list = service.readAllByOrganizationId(orgId);

            List<DTOResponse> responses = new ArrayList<>();
            list.forEach(it -> responses.add(new DTOResponse(it.getId(), it.getUser().toString(),
                    it.getDiscipline().getName(), it.getCourse().getName(), it.getClazz().getCode())));

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
