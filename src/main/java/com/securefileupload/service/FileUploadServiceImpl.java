package com.securefileupload.service;

import com.securefileupload.config.BucketName;
import com.securefileupload.domain.FileDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public FileDetail saveTodo(String title, String description, File file) throws IOException {
        if (file.length() == 0) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        /*if (Arrays.asList(IMAGE_PNG.getMimeType(), IMAGE_BMP.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }*/
        //get file metadata
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
    public byte[] downloadTodoImage(UUID id) {
        FileDetail fileDetail = tempStore.get(id);
        return fileStore.download(fileDetail.getFilePath(), fileDetail.getFileName());
    }

    @Override
    public List<FileDetail> getAllTodos() {
        List<FileDetail> fileDetails = new ArrayList<>();
        tempStore.forEach((uuid, fileDetail) -> fileDetails.add(fileDetail));
        return fileDetails;
    }
}
