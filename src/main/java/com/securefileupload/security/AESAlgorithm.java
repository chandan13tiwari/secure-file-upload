package com.securefileupload.security;

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
import java.security.NoSuchAlgorithmException;


public class AESAlgorithm {
    private static final String ALGO_NAME = "AES";
    private static final String TRANSFORMATION_NAME = "AES";

    public static void doEncryption(byte[] aesKey, File readFile, File writeFile) throws CryptoException {
        doExecute(Cipher.ENCRYPT_MODE, aesKey, readFile, writeFile);
    }

    public static void doDecryption(byte[] aesKey, File readFile, File writeFile) throws CryptoException {
        doExecute(Cipher.DECRYPT_MODE, aesKey, readFile, writeFile);
    }

    private static void doExecute(int cipherModeNumber, byte[] aesKey, File readFile, File writeFile) throws CryptoException {
        try {
            Key aesFinalKey = new SecretKeySpec(aesKey, ALGO_NAME);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_NAME);
            cipher.init(cipherModeNumber, aesFinalKey);

            FileInputStream readingStream = new FileInputStream(readFile);
            byte[] readingBytes = new byte[(int) readFile.length()];
            readingStream.read(readingBytes);

            byte[] writingBytes = cipher.doFinal(readingBytes);

            FileOutputStream writingStream = new FileOutputStream(writeFile);
            writingStream.write(writingBytes);

            readingStream.close();
            writingStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            throw new CryptoException("Exception while encrypting or decrypting:", e);
        }
    }
}
