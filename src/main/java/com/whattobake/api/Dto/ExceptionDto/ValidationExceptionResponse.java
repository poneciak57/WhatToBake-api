package com.whattobake.api.Dto.ExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationExceptionResponse {
    private List<ValidationFieldError> fieldErrors;
}
