package com.escolatecnica.api.root.service;

import com.escolatecnica.api.root.utils.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
    void init();

    void save(MultipartFile file);

    Resource load(String filename) throws NotFoundException;
}
