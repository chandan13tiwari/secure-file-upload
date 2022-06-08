package com.securefileupload.exception;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
