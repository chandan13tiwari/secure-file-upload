package com.securefileupload.security;

import com.securefileupload.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KeyGenerator {

    private static final String RSA = "RSA";
    private static final String AES_KEY_FILE = "aesKey.txt";
    private static final String PUBLIC_KEY_FILE = "publicKey.txt";
    private static final String PRIVATE_KEY_FILE = "privateKey.txt";
    private static final String AES_ENCRYPTED_FILE = "AES_Encrypted.txt";
    private static final String AES_DECRYPTED_FILE = "AES_Decrypted.txt";
    private static final int KEY_LENGTH = 1024;
    private static final String MESSAGE_DIGEST = "MD5";

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] aesKey;

    public void generateRandomKeyAES() throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST);
        this.aesKey = messageDigest.digest(Constants.KEY.getBytes(UTF_8));
        writeToFile(Constants.KEY_PATH + AES_KEY_FILE, this.aesKey);
    }

    public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        this.keyGen = KeyPairGenerator.getInstance(RSA);
        this.keyGen.initialize(KEY_LENGTH);
        this.createKeys();
    }

    private void createKeys() throws IOException {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();

        writeToFile(Constants.KEY_PATH + PUBLIC_KEY_FILE, this.getPublicKey().getEncoded());
        writeToFile(Constants.KEY_PATH + PRIVATE_KEY_FILE, this.getPrivateKey().getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] getAESKey() {
        return this.aesKey;
    }

    public static void writeToFile(String path, byte[] key) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public void encryptAESKey() throws IOException, GeneralSecurityException {
        RSAAlgorithm rsa = new RSAAlgorithm();
        rsa.encryptAESKey(this.aesKey, new File(Constants.KEY_PATH + AES_ENCRYPTED_FILE), this.privateKey);

    }

    public void decryptAESKey() throws IOException, GeneralSecurityException {
        RSAAlgorithm rsa = new RSAAlgorithm();
        rsa.decryptAESKey(rsa.fileToBytes(new File(Constants.KEY_PATH + AES_ENCRYPTED_FILE)),
                new File(Constants.KEY_PATH + AES_DECRYPTED_FILE), this.publicKey);

    }

}
