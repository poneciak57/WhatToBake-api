package com.whattobake.api.Exception.Handlers;

import com.whattobake.api.Dto.ExceptionDto.ValidationExceptionResponse;
import com.whattobake.api.Dto.ExceptionDto.ValidationFieldError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class ValidationHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse handleException(WebExchangeBindException e) {
        var fieldErrors = e.getFieldErrors()
                .stream()
                .map(ValidationFieldError::new)
                .toList();
        return ValidationExceptionResponse.builder()
                        .fieldErrors(fieldErrors)
                .build();
    }
}
