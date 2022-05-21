package com.securefileupload.service;

import com.securefileupload.domain.FileDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileUploadService {
    FileDetail saveTodo(String title, String description, File file) throws IOException;

    byte[] downloadTodoImage(UUID id);

    List<FileDetail> getAllTodos();
}
