package com.securefileupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.securefileupload.entity"})
@EnableJpaRepositories({"com.securefileupload.repository"})
public class SecureFileUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecureFileUploadApplication.class, args);
    }
}
