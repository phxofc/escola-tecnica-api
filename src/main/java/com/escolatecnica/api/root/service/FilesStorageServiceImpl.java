package com.escolatecnica.api.root.service;

import com.escolatecnica.api.root.utils.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{
    private final Path root = Paths.get("uploads");

    public FilesStorageServiceImpl(){
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        if(Objects.isNull(root.getRoot())) init();

        try {
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) throws NotFoundException {
        Path file = root.resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NotFoundException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new NotFoundException("Could not read file: " + filename, e);
        }
    }


}
