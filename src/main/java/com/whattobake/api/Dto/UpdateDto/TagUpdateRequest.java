package com.whattobake.api.Dto.UpdateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagUpdateRequest {
    private Long id;
    private String name;
}
