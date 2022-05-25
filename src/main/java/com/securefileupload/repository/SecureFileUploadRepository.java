package com.securefileupload.repository;

import com.securefileupload.entity.SecureFileUploadEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SecureFileUploadRepository extends CrudRepository<SecureFileUploadEntity, UUID> {
}
