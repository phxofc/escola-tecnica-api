package com.escolatecnica.api.documentation.service;

import com.escolatecnica.api.documentation.model.Documentation;
import com.escolatecnica.api.documentation.repository.DocumentationRepository;
import com.escolatecnica.api.root.service.FilesStorageService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DocumentationServiceImp implements DocumentationService {

    private final FilesStorageService storage;

    private final DocumentationRepository repository;

    @Override
    public Documentation createDocumetation(MultipartFile[] files, UUID organizationId) throws APIException, NotFoundException {
        if(Objects.isNull(organizationId)) throw new APIException("[organizationId] is null.");

        if(files.length == 0) throw new APIException("[files] is empty.");

        if(Objects.isNull(files[0]) || Objects.isNull(files[1]) || Objects.isNull(files[2]))
            throw new ArrayIndexOutOfBoundsException("Number of invalid files.");


        Documentation documentation = Documentation.builder()
                .crFileName(files[0].getOriginalFilename())
                .cpfFileName(files[1].getOriginalFilename())
                .rgFileName(files[2].getOriginalFilename())
                .organizationId(organizationId)
                .createdAt(ZonedDateTime.now())
                .validated(Boolean.FALSE)
                .build();

        documentation = repository.save(documentation);

        if(Objects.nonNull(documentation.getId())){
            for(MultipartFile file : files) storage.save(file);
        }

        return documentation;
    }
}
