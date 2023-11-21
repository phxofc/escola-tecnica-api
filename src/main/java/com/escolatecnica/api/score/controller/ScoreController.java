package com.escolatecnica.api.score.controller;

import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.score.dto.DTOScoreRequest;
import com.escolatecnica.api.score.model.Score;
import com.escolatecnica.api.score.service.ScoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/score")
public class ScoreController {

    private final JWTUtil jwtUtil;
    private final ScoreService service;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> create(HttpServletRequest request, @RequestBody DTOScoreRequest body) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Score score = Score.builder()
                    .comment(body.comment())
                    .value(body.value())
                    .registeredBy(userId)
                    .build();

            service.create(score, body.materialId(), body.userId(), orgId);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
