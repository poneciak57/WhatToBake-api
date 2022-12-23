package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Interfaces.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters implements Filter<RecipeFilters> {
    Integer page;
    List<Integer> products;
    List<RecipeProductOrder> productOrder;

    @Override
    public RecipeFilters fillDefaults() {
        page = (page == null ? 0 : page);
        products = (products == null ? List.of() : products);
        productOrder = (productOrder == null ? List.of() : productOrder);
        return this;
    }
}
