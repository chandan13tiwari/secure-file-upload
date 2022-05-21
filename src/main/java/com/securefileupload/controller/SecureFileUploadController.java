package com.securefileupload.controller;

import com.securefileupload.domain.FileDetail;
import com.securefileupload.exception.CryptoException;
import com.securefileupload.service.FileUploadService;
import com.securefileupload.util.Constants;
import com.securefileupload.util.CryptoUtil;
import lombok.AllArgsConstructor;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("api/v1/secure")
@AllArgsConstructor
@CrossOrigin("*")
public class SecureFileUploadController {
    FileUploadService service;

    @GetMapping("/")
    public String homePage() {
        return "upload";
    }

    @GetMapping("/getTodos")
    public ResponseEntity<List<FileDetail>> getAllFiles(Model model) {
        model.addAttribute("files", service.getAllTodos());
        return new ResponseEntity<>(service.getAllTodos(), HttpStatus.OK);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileDetail> uploadFileToS3(Model model, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam("file") MultipartFile multipartFile) throws IOException {

        //Start encrypting File
        File file = convertMultiPartToFile(multipartFile);
        File encryptedFile = new File(Constants.FILE_PATH + multipartFile.getOriginalFilename() + ".encrypted");
        File decryptedFile = new File(Constants.FILE_PATH + multipartFile.getOriginalFilename() + ".decrypted");

        String message = "";

        try {
            CryptoUtil.encrypt(Constants.KEY, file, encryptedFile);
            FileDetail uploadedFile = service.saveTodo(title, description, file);
            message = "Your file has been uploaded successfully!";

            model.addAttribute(message);
            return new ResponseEntity<>(uploadedFile, HttpStatus.OK);
            //CryptoUtil.decrypt(Constants.KEY, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            message = ex.getMessage();
            ex.printStackTrace();
            model.addAttribute(message);
            return new ResponseEntity<>(new FileDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping(value = "{id}/image/download")
    public byte[] downloadFileFromS3(@PathVariable("id") UUID id) {
        return service.downloadTodoImage(id);
    }

    private static File convertMultiPartToFile(MultipartFile file ) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}
