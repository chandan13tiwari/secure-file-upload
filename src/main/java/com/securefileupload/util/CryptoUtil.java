package com.securefileupload.util;

import com.securefileupload.exception.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CryptoUtil {

    private static final String TRANSFORMATION = "AES";
    private static final String ALGORITHM = "AES";
    private static final String MESSAGE_DIGEST = "MD5";

    public static void encrypt(File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile);
    }

    public static void decrypt(File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, File inputFile, File outputFile) throws CryptoException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST);
            Key secretKey = new SecretKeySpec(messageDigest.digest(Constants.KEY.getBytes(UTF_8)), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}
