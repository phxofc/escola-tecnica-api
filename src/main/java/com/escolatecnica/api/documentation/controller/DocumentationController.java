package com.escolatecnica.api.documentation.controller;


import com.escolatecnica.api.root.service.FilesStorageService;
import com.escolatecnica.api.root.utils.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/document")
public class DocumentationController {

    private final FilesStorageService filesStorageService;

    @GetMapping(value = "/read/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        Resource resource;
        try {
            resource = filesStorageService.load(fileName);

            String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("File not found.", HttpStatus.NOT_FOUND);
        }
    }

}
