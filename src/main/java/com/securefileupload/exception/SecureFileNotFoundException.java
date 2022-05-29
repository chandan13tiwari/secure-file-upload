package com.securefileupload.exception;

public class SecureFileNotFoundException extends Exception {
    public SecureFileNotFoundException() {
    }

    public SecureFileNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SecureFileNotFoundException(String message) {
        super(message);
    }
}
