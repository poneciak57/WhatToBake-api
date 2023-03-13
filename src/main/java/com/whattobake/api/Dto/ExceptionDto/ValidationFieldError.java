package com.whattobake.api.Dto.ExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFieldError {
    private String field;
    private String message;

    public ValidationFieldError(FieldError fieldError){
        this(fieldError.getField(),fieldError.getDefaultMessage());
    }
}
