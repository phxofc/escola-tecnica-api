package com.escolatecnica.api.documentation.service;

import com.escolatecnica.api.documentation.model.Documentation;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import com.escolatecnica.api.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface DocumentationService {

    Documentation createDocumetation(MultipartFile[] files, UUID organizationId) throws APIException, NotFoundException;
}
