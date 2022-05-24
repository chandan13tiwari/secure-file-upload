package com.securefileupload.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    S3_BUCKET("spring-secure-file-upload");
    private final String bucketName;
}
