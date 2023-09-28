package com.e2s.cam.Error;

import org.springframework.http.HttpStatus;

public class ErrorResponses {
    private HttpStatus httpStatus;
    private String message;

    public ErrorResponses(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }}