package com.example.demo.exception;

import org.springframework.web.client.HttpClientErrorException;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String message, HttpClientErrorException.NotFound ex) {
        super(message, ex);
    }
}
