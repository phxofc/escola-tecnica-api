package com.escolatecnica.api.answer.service;

import com.escolatecnica.api.answer.model.Answer;
import com.escolatecnica.api.answer.repository.AnswerRepository;
import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.material.repository.MaterialRepository;
import com.escolatecnica.api.root.service.FilesStorageService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import com.escolatecnica.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AnswerServiceImp implements AnswerService{

    private final AnswerRepository repository;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final FilesStorageService storage;

    @Override
    public boolean create(Answer answer, MultipartFile file, UUID userId, UUID materialId, UUID organizationId) {
        checkRequiredParameters(userId, materialId, organizationId);
        if (Objects.isNull(answer)) throw new APIException("[Answer] is NULL.");

        Optional<User> userSaved = userRepository.readByIdAndOrganizationId(userId, organizationId);
        if(userSaved.isEmpty()) throw new NotFoundException("[User] not found with id: "+userId);

        Material material = materialRepository.readByIdAndOrganizationId(materialId, organizationId);
        if(Objects.isNull(material)) throw new NotFoundException("[Material] not found with id: "+materialId);

        if(Objects.nonNull(file)){
            answer.setAttachedFileName(file.getOriginalFilename());
        }

        answer.setCreatedAt(ZonedDateTime.now());
        answer.setRegisteredBy(userId);
        answer.setMaterial(material);
        answer.setUser(userSaved.get());

        repository.save(answer);

        if(Objects.nonNull(file) && Objects.nonNull(file.getOriginalFilename())){
            storage.save(file);
        }

        return Boolean.TRUE;
    }

    @Override
    public boolean thereIsAnswerAssociatedWithMaterial(UUID userId, UUID materialId, UUID organizationId) throws APIException {
        checkRequiredParameters(userId, materialId, organizationId);
        return repository.existsByUser_IdAndMaterial_IdAndOrganizationId(userId, materialId, organizationId);
    }

    @Override
    public List<Answer> readAllAnswerFromTask(UUID materialId, UUID organizationId) throws APIException {
        if (Objects.isNull(materialId)) throw new APIException("[material id] is NULL.");

        if (Objects.isNull(organizationId)) throw new APIException("[organization id] is NULL.");

        return repository.findByMaterial_IdAndOrganizationId(materialId, organizationId);
    }

    private void checkRequiredParameters(UUID userId, UUID materialId, UUID organizationId){
        if (Objects.isNull(userId)) throw new APIException("[User id] is NULL.");

        if (Objects.isNull(materialId)) throw new APIException("[Material id] is NULL.");

        if (Objects.isNull(organizationId)) throw new APIException("[Organization id] is NULL.");
    }
}
