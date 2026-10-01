package org.example.poketrade.exception;

public class BusinessException extends RuntimeException {
    public static final String WRONG_OWNER = "This entity does not belong to you";

    public BusinessException(String message) {
        super(message);
    }
}
