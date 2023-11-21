package com.escolatecnica.api.answer.controller;

import com.escolatecnica.api.answer.dto.DTOAnswerRequest;
import com.escolatecnica.api.answer.dto.DTOAnswerResponse;
import com.escolatecnica.api.answer.model.Answer;
import com.escolatecnica.api.answer.service.AnswerService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.JWTUtil;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.score.dto.DTOScoreResponse;
import com.escolatecnica.api.score.model.Score;
import com.escolatecnica.api.score.service.ScoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final JWTUtil jwtUtil;
    private final AnswerService service;
    private final ScoreService scoreService;

    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Boolean> create(HttpServletRequest request, @ModelAttribute DTOAnswerRequest body) {
        try {
            UUID organizationId = jwtUtil.getOrganizationIdFromRequest(request);
            UUID userId = jwtUtil.getUserIdFromRequest(request);

            Answer answer = Answer.builder()
                    .description(body.description())
                    .organizationId(organizationId)
                    .build();

            return new ResponseEntity<>(service.create(answer, body.file(), userId, body.materialId(), organizationId), HttpStatus.OK);
        } catch (APIException | NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/by-material/{materialId}", produces = "application/json")
    public ResponseEntity<List<DTOAnswerResponse>> readAllByMaterial(HttpServletRequest request, @PathVariable UUID materialId) {
        try {
            UUID orgId = jwtUtil.getOrganizationIdFromRequest(request);

            List<Answer> list = service.readAllAnswerFromTask(materialId, orgId);
            List<DTOAnswerResponse> dtoList = new ArrayList<>();

            list.forEach(it -> dtoList.add(new DTOAnswerResponse(
                    it.getUser().getId(),
                    it.getUser().toString(),
                    it.getDescription(),
                    it.getAttachedFileName(),
                    it.getCreatedAt(),
                    getScore(materialId, it.getUser().getId(), orgId)
                    )));

            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private DTOScoreResponse getScore(UUID materialId, UUID userId, UUID organizationId){
        Score score = scoreService.readByMaterialIdAndUserIdAndOrganizationId(materialId, userId, organizationId);
        if(Objects.nonNull(score)){
            return new DTOScoreResponse(score.getValue(), score.getComment());
        }
        return null;
    }
}
