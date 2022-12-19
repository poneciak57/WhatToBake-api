package com.whattobake.api.Dto;

import com.whattobake.api.Enum.RecipeProductOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters {
    Integer page;
    List<Integer> products;
    List<RecipeProductOrder> productOrder;
}
