package com.example.examsystem.common;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}