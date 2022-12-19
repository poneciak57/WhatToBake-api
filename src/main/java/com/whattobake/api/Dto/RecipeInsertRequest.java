package com.whattobake.api.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInsertRequest {
    private String title;
    private String link;
    private String image;
    private List<Long> products;
}
