package com.tomerab.exceptions;

public class JsonSyntaxError extends RuntimeException {
    public JsonSyntaxError(String errorMessage) {
        super(errorMessage);
    }
}
