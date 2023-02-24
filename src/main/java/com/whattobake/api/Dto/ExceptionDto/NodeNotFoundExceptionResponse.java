package com.whattobake.api.Dto.ExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeNotFoundExceptionResponse {
    private String message;
}
