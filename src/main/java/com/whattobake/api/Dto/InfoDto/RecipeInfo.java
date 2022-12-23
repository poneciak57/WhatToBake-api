package com.whattobake.api.Dto.InfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInfo {
    private Long count;
    private Long countWithFilters;
}
