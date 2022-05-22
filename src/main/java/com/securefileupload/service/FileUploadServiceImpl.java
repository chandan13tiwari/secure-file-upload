package com.securefileupload.service;

import com.securefileupload.config.BucketName;
import com.securefileupload.domain.FileDetail;
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
    private final FileStore fileStore;

    private static final Map<UUID, FileDetail> tempStore = new HashMap<>();

    @Override
    public FileDetail saveFile(String title, String description, File file) throws IOException {
        if (file.length() == 0) {
            throw new IllegalStateException("Cannot upload empty file");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", Files.probeContentType(file.toPath()));
        metadata.put("Content-Length", String.valueOf(file.length()));

        String path = String.format("%s/%s", BucketName.FILE_BUCKET.getBucketName(), UUID.randomUUID());
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
        tempStore.put(fileDetail.getId(), fileDetail);
        return tempStore.get(fileDetail.getId());
    }

    @Override
    public File downloadFile(UUID id) {
        FileDetail fileDetail = tempStore.get(id);
        return fileStore.download(fileDetail.getFilePath(), fileDetail.getFileName());
    }

    @Override
    public List<FileDetail> getAllFiles() {
        List<FileDetail> fileDetails = new ArrayList<>();
        tempStore.forEach((uuid, fileDetail) -> fileDetails.add(fileDetail));
        return fileDetails;
    }
}
