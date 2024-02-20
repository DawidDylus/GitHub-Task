package com.example.demo.exception;

import org.springframework.web.client.HttpClientErrorException;

public class RepoNotFoundException extends Throwable {
    public RepoNotFoundException(String message, HttpClientErrorException.NotFound ex) {
        super(message, ex);
    }
}
