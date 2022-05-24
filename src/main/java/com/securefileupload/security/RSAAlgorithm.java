package com.securefileupload.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAAlgorithm {
    private static final String RSA = "RSA";
    private final Cipher rsaCipher;

    public RSAAlgorithm() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.rsaCipher = Cipher.getInstance(RSA);
    }

    public PrivateKey getPrivateKey(String fileLocation) throws Exception {
        byte[] privateKeyBytes = Files.readAllBytes(new File(fileLocation).toPath());
        PKCS8EncodedKeySpec codecSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(codecSpec);
    }

    public PublicKey getPublicKey(String fileLocation) throws Exception {
        byte[] publicKeyBytes = Files.readAllBytes(new File(fileLocation).toPath());
        X509EncodedKeySpec codecSpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(codecSpec);
    }

    public void encryptAESKey(byte[] inputKEY, File outputFile, PrivateKey privatekey) throws IOException, GeneralSecurityException {
        this.rsaCipher.init(Cipher.ENCRYPT_MODE, privatekey);
        writeToFile(outputFile, this.rsaCipher.doFinal(inputKEY));
    }

    public void decryptAESKey(byte[] inputKEY, File outputFile, PublicKey publickey) throws IOException, GeneralSecurityException {
        this.rsaCipher.init(Cipher.DECRYPT_MODE, publickey);
        writeToFile(outputFile, this.rsaCipher.doFinal(inputKEY));
    }

    private static void writeToFile(File outputFile, byte[] byteToWrite) throws IllegalBlockSizeException, BadPaddingException, IOException {
        FileOutputStream writingFile = new FileOutputStream(outputFile);
        writingFile.write(byteToWrite);
        writingFile.flush();
        writingFile.close();
    }

    public byte[] fileToBytes(File file) throws IOException {
        FileInputStream readFile = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        readFile.read(fileBytes);
        readFile.close();
        return fileBytes;
    }
}
