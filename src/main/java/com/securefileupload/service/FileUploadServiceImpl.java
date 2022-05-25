package com.securefileupload.service;

import com.securefileupload.config.BucketName;
import com.securefileupload.domain.FileDetail;
import com.securefileupload.entity.SecureFileUploadEntity;
import com.securefileupload.exception.SecureFileNotFoundException;
import com.securefileupload.repository.SecureFileUploadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
@AllArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private SecureFileUploadRepository secureFileUploadRepository;
    private final FileStore fileStore;

    @Override
    public FileDetail saveFile(String title, String description, File file) throws IOException {
        if (file.length() == 0) {
            throw new IllegalStateException("Cannot upload empty file");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", Files.probeContentType(file.toPath()));
        metadata.put("Content-Length", String.valueOf(file.length()));

        String path = String.format("%s/%s", BucketName.S3_BUCKET.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getName());
        try {
            fileStore.upload(path, fileName, Optional.of(metadata), new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        FileDetail fileDetail = FileDetail.builder()
                .id(UUID.randomUUID())
                .description(description)
                .title(title)
                .filePath(path)
                .fileName(fileName)
                .build();

        secureFileUploadRepository.save(SecureFileUploadEntity.fromFileDetails(fileDetail));
        return fileDetail;
    }

    @Override
    public File downloadFile(UUID id) throws SecureFileNotFoundException {
        Optional<SecureFileUploadEntity> secureFileUpload = secureFileUploadRepository.findById(id);
        if (secureFileUpload.isPresent()) {
            return fileStore.download(secureFileUpload.get().getSecureFileS3Path(), secureFileUpload.get().getSecureFileName());
        } else {
            throw new SecureFileNotFoundException("File not present in DB");
        }
    }

    @Override
    public List<SecureFileUploadEntity> getAllFiles() {
        return (List<SecureFileUploadEntity>) secureFileUploadRepository.findAll();
    }
}
