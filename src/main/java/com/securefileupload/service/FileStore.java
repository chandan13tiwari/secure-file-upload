package com.securefileupload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.securefileupload.exception.SecureFileNotFoundException;
import com.securefileupload.util.Constants;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FileStore {
    private final AmazonS3 amazonS3;

    public void upload(final String path, final String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public File download(final String path, final String key) {
        try {
            S3Object object = amazonS3.getObject(path, key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            File downloadedFile = new File(Constants.FILE_PATH + key);
            FileUtils.copyInputStreamToFile(inputStream, downloadedFile);
            return downloadedFile;
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to download the file", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error in file conversion", e);
        }
    }

    public void delete(final String bucketName, final String key) {
        try {
            final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to delete the file");
        }
    }

}