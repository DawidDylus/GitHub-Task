package com.example.demo.exception;

import org.springframework.web.client.HttpClientErrorException;

public class BranchNotFoundException extends Throwable {
    public BranchNotFoundException(String message, HttpClientErrorException.NotFound ex) {
        super(message, ex);
    }
}
