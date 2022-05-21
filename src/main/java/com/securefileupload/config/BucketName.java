package com.securefileupload.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    FILE_BUCKET("spring-amazon-storage");
    private final String bucketName;
}
