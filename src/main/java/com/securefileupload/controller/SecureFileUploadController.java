package com.securefileupload.controller;

import com.securefileupload.domain.FileDetail;
import com.securefileupload.entity.SecureFileUploadEntity;
import com.securefileupload.exception.CryptoException;
import com.securefileupload.exception.SecureFileNotFoundException;
import com.securefileupload.security.AESAlgorithm;
import com.securefileupload.security.KeyGenerator;
import com.securefileupload.service.FileUploadService;
import com.securefileupload.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/secure/user")
@CrossOrigin("*")
public class SecureFileUploadController {

    private final FileUploadService service;

    private final KeyGenerator keyGenerator;

    @Autowired
    public SecureFileUploadController(FileUploadService service, KeyGenerator keyGenerator) {
        this.service = service;
        this.keyGenerator = keyGenerator;
    }

    @PostMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    @GetMapping("/home")
    public String homePage() {
        return "upload";
    }

    @GetMapping("/getAllFiles")
    public String getAllFiles(Model model) {
        model.addAttribute("files", service.getAllFiles());
        return "files";
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileDetail> uploadFileToS3(@RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("file") MultipartFile multipartFile) throws IOException, CryptoException, GeneralSecurityException {
        File file = convertMultiPartToFile(multipartFile);
        String fileName = file.getName();

        keyGenerator.generateRandomKeyAES();
        keyGenerator.generateKeyPair();
        keyGenerator.encryptAESKey();

        File encryptedFile = new File(Constants.FILE_PATH + fileName.substring(0, fileName.lastIndexOf('.')) + ".encrypted");

        try {
            AESAlgorithm.doEncryption(keyGenerator.getAESKey(), file, encryptedFile);
            FileDetail uploadedFile = service.saveFile(title, description, encryptedFile);
            return new ResponseEntity<>(uploadedFile, HttpStatus.OK);
        } catch (CryptoException ex) {
            ex.printStackTrace();
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } finally {
            file.delete();
            encryptedFile.deleteOnExit();
        }


    }

    @GetMapping(value = "{id}/download")
    public ResponseEntity<String> downloadFileFromS3(@PathVariable("id") UUID id) throws CryptoException, SecureFileNotFoundException {
        File downloadedFile = service.downloadFile(id);
        String fileName = downloadedFile.getName();
        File decryptedFile = new File(Constants.FILE_PATH + fileName.substring(0, fileName.lastIndexOf('.')) + ".decrypted");

        try {
            AESAlgorithm.doDecryption(keyGenerator.getAESKey(), downloadedFile, decryptedFile);
            keyGenerator.decryptAESKey();
            return new ResponseEntity<>("File Downloaded Successfully in location: " + Constants.FILE_PATH, HttpStatus.OK);
        } catch (CryptoException ex) {
            ex.printStackTrace();
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } catch (GeneralSecurityException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } finally {
            downloadedFile.delete();
            decryptedFile.deleteOnExit(); //remove this line if you want to permanently store downloaded files
        }
    }

    @GetMapping(value = "{id}/delete")
    public ResponseEntity<String> deleteFileFromS3(@PathVariable("id") UUID id) throws SecureFileNotFoundException {
        SecureFileUploadEntity deletedFile = service.deleteFile(id);
        String fileName = deletedFile.getSecureFileName();
        String path = deletedFile.getSecureFileS3Path();
        return new ResponseEntity<>(path + "/" + fileName + " has been deleted successfully", HttpStatus.OK);
    }

    private static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
