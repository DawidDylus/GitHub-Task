package com.example.demo.exception;

import org.springframework.web.client.HttpClientErrorException;

public class RateLimitExceededException extends Throwable {
    public RateLimitExceededException(String message, HttpClientErrorException.Forbidden ex) {
        super(message, ex);
    }
}
