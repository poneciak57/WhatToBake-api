package com.whattobake.api.Exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NodeNotFound extends RuntimeException{
    private String errorMessage;
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
