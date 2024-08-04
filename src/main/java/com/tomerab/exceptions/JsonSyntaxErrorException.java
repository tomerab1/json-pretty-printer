package com.tomerab.exceptions;

public class JsonSyntaxErrorException extends RuntimeException {
    public JsonSyntaxErrorException(String errorMessage) {
        super(errorMessage);
    }
}
