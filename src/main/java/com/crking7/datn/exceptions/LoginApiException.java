package com.crking7.datn.exceptions;

import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginApiException extends RuntimeException {

    public LoginApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginApiException(String message) {
        super(message);
    }

    public static LoginApiException handleJwtException(Exception e) {
        if (e instanceof MalformedJwtException) {
            return new LoginApiException("Invalid JWT token: " + e.getMessage(), e);
        } else if (e instanceof IncorrectClaimException) {
            return new LoginApiException("Invalid JWT signature: " + e.getMessage(), e);
        } else if (e instanceof UnsupportedJwtException) {
            return new LoginApiException("Unsupported JWT token: " + e.getMessage(), e);
        } else if (e instanceof JwtException) {
            return new LoginApiException("Unknown JWT exception: " + e.getMessage(), e);
        }else if (e instanceof IllegalArgumentException){
            return new LoginApiException("Illegal JWT exception: " + e.getMessage(), e);
        }else if (e instanceof InternalAuthenticationServiceException){
            return new LoginApiException("Internal Authentication exception: " + e.getMessage(), e);
        } else {
            return new LoginApiException("Unknown exception: " + e.getMessage(), e);
        }
    }
}

