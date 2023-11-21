package com.escolatecnica.api.score.service;

import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.material.repository.MaterialRepository;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.score.model.Score;
import com.escolatecnica.api.score.repository.ScoreRepository;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ScoreServiceImp implements ScoreService{

    private final ScoreRepository repository;

    private final MaterialRepository materialRepository;

    private final UserRepository userRepository;

    @Override
    public boolean thereIsANoteForThisTask(UUID materialId, UUID userId, UUID organizationId) {
        validateFields(materialId, userId, organizationId);
        return repository.existsByMaterialIdAndUserIdAndOrganizationId(materialId, userId, organizationId);
    }

    @Override
    public boolean create(Score score, UUID materialId, UUID userId, UUID organizationId) throws APIException {
        if (Objects.isNull(score) || Objects.isNull(score.getValue())) throw new APIException("[score] is NULL.");

        validateFields(materialId, userId, organizationId);

        Optional<Score> optional = repository.findByMaterialIdAndUserIdAndOrganizationId(materialId, userId, organizationId);

        if(optional.isEmpty()){
            Material material = materialRepository.readByIdAndOrganizationId(materialId, organizationId);

            if(Objects.isNull(material)) throw new APIException("Not found material with id: "+materialId);
            score.setMaterial(material);

            Optional<User> optionalUser = userRepository.readByIdAndOrganizationId(userId, organizationId);
            if(optionalUser.isEmpty()) throw new APIException("Not found user with id: "+userId);
            score.setUser(optionalUser.get());

            score.setCreatedAt(ZonedDateTime.now());
            score.setOrganizationId(organizationId);

            repository.save(score);

            return true;
        }

        optional.get().setUpdatedAt(ZonedDateTime.now());
        optional.get().setValue(score.getValue());
        optional.get().setComment(score.getComment());

        repository.save(optional.get());

        return true;
    }

    @Override
    public Score readByMaterialIdAndUserIdAndOrganizationId(UUID materialId, UUID userId, UUID organizationId) throws APIException {
        validateFields(materialId, userId, organizationId);
        Optional<Score> optional = repository.findByMaterialIdAndUserIdAndOrganizationId(materialId, userId, organizationId);
        return optional.orElse(null);
    }

    private void validateFields(UUID materialId, UUID userId, UUID organizationId) throws APIException{
        if (Objects.isNull(organizationId)) throw new APIException("[organization id] is NULL.");

        if (Objects.isNull(materialId)) throw new APIException("[material id] is NULL.");

        if (Objects.isNull(userId)) throw new APIException("[user id] is NULL.");
    }
}
