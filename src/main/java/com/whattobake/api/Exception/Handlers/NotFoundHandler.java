package com.whattobake.api.Exception.Handlers;

import com.whattobake.api.Dto.ErrorDto;
import com.whattobake.api.Exception.CategoryNotFoundException;
import com.whattobake.api.Exception.ProductNotFoundException;
import com.whattobake.api.Exception.RecipeNotFoundException;
import com.whattobake.api.Exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto categoryNotFoundExceptionHandler(Exception ex){
        return ErrorDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto productNotFoundExceptionHandler(Exception ex){
        return ErrorDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto recipeNotFoundExceptionHandler(Exception ex){
        return ErrorDto.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto tagNotFoundExceptionHandler(Exception ex){
        return ErrorDto.builder()
                .message(ex.getMessage())
                .build();
    }
}