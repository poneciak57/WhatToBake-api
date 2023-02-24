package com.whattobake.api.Exception.Handlers;

import com.whattobake.api.Dto.ExceptionDto.NodeNotFoundExceptionResponse;
import com.whattobake.api.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {
    @ExceptionHandler(NodeNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NodeNotFoundExceptionResponse nodeNotFoundExceptionHandler(Exception ex){
        return NodeNotFoundExceptionResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}