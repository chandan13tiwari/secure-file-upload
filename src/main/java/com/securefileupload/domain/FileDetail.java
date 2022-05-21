package com.securefileupload.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDetail {
    private UUID id;
    private String title;
    private String description;
    private String filePath;
    private String fileName;

}
