package com.whattobake.api.Dto.InfoDto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInfo {

    @Min(0)
    private Long count;

    @Min(0)
    private Long countWithFilters;
}
