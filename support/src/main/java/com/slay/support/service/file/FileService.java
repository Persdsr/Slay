package com.slay.support.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

@Service

public class FileService {
    private final StorageService storageService;

    public FileService(StorageService storageService) {
        this.storageService = storageService;
    }

    @Value("${slay.app.httpprotocol}")
    private String protocol;

    public String saveFile(MultipartFile file) {
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        storageService.store(file, uniqueFileName);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme(protocol)
                .path("/download/")
                .path(uniqueFileName)
                .toUriString();
    }


    public List<String> saveAllFileList(MultipartFile[] files) {
        List<String> filesUris = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
                storageService.store(file, uniqueFileName);

                String uriFile = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .scheme(protocol)
                        .path("/download/")
                        .path(uniqueFileName)
                        .toUriString();

                filesUris.add(uriFile);
            }
        }

        return filesUris;
    }

    public Set<String> saveAllFileSet(MultipartFile[] files) {
        Set<String> filesUris = new HashSet<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
                storageService.store(file, uniqueFileName);

                String uriFile = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .scheme(protocol)
                        .path("/download/")
                        .path(uniqueFileName)
                        .toUriString();

                filesUris.add(uriFile);
            }
        }

        return filesUris;
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
