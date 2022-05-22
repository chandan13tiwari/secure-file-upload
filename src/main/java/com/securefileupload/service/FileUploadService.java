package com.securefileupload.service;

import com.securefileupload.domain.FileDetail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileUploadService {
    FileDetail saveFile(String title, String description, File file) throws IOException;

    File downloadFile(UUID id);

    List<FileDetail> getAllFiles();
}
