package com.omniteam.backofisbackend.security.jwt.advice;

import java.util.Date;

import com.omniteam.backofisbackend.security.jwt.exception.TokenRefreshException;
import com.omniteam.backofisbackend.shared.result.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResult handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorResult(ex.getMessage());
    }
}
