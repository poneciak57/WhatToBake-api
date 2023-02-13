package com.whattobake.api.Exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductNotFoundException extends RuntimeException{
    private String errorMessage;
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
