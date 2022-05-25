package com.securefileupload.entity;

import com.securefileupload.domain.FileDetail;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "sec_file_dtls")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SecureFileUploadEntity {

    @Id
    @Column(name = "sec_file_id")
    private UUID secureFileId;

    @Column(name = "sec_file_title")
    private String secureFileTitle;

    @Column(name = "sec_file_desc")
    private String secureFileDescription;

    @Column(name = "sec_file_name")
    private String secureFileName;

    @Column(name = "sec_file_s3_path")
    private String secureFileS3Path;

    public static SecureFileUploadEntity fromFileDetails(FileDetail fileDetail) {
        return SecureFileUploadEntity.builder()
                .secureFileId(fileDetail.getId())
                .secureFileTitle(fileDetail.getTitle())
                .secureFileDescription(fileDetail.getDescription())
                .secureFileName(fileDetail.getFileName())
                .secureFileS3Path(fileDetail.getFilePath())
                .build();
    }

}
