package com.escolatecnica.api.enrollment.controller;

import com.escolatecnica.api.enrollment.dto.EnrollmentRequest;
import com.escolatecnica.api.enrollment.dto.EnrollmentResponse;
import com.escolatecnica.api.enrollment.model.Enrollment;
import com.escolatecnica.api.enrollment.service.EnrollmentService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.controller.UserController;
import com.escolatecnica.api.user.model.Role;
import com.escolatecnica.api.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final EnrollmentService service;

    private final JWTUtil jwtUtil;

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Boolean> create(@ModelAttribute EnrollmentRequest dto) {
        try {
            User user = User.builder()
                    .organizationId(dto.organizationId())
                    .cpf(dto.cpf())
                    .email(dto.email())
                    .lastName(dto.lastName())
                    .name(dto.name())
                    .role(Role.STUDENT)
                    .password(dto.password())
                    .enabledAccess(Boolean.FALSE)
                    .createdAt(ZonedDateTime.now())
                    .build();

            boolean result = service.create(dto.organizationId(), dto.courseId(), dto.clazzId(), user, dto.files());

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<List<EnrollmentResponse>> readByUserId(HttpServletRequest request, @PathVariable UUID id) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            List<Enrollment> list = service.readByOrganizationIdAndUserId(orgId, id);
            List<EnrollmentResponse> dtoList = new ArrayList<>();

            list.forEach(cr -> dtoList.add(new EnrollmentResponse(
                    cr.getId(),
                    cr.getCourse().toString(),
                    cr.getRegistrationApproved(),
                    cr.getClazz().getCode(),
                    cr.getCourse().getId(),
                    cr.getClazz().getId())));

            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    record EnrollmentUpdate(UUID studentId, UUID courseId, UUID clazzId){}
    @PostMapping(value = "/update-access", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> updateAccess(HttpServletRequest request, @RequestBody EnrollmentUpdate body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            Boolean result = service.updateAccess(body.studentId(), body.courseId(), body.clazzId(), orgId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
