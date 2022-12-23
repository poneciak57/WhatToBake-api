package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Enum.TagOption;
import com.whattobake.api.Interfaces.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters implements NotNull<RecipeFilters> {
    private Long page;
    private List<Long> products;
    private List<Long> tags;
    private List<RecipeProductOrder> productOrder;
    private TagOption tagOption;

    @Override
    public RecipeFilters fillDefaults() {
        page = (page == null ? 0 : page);
        products = (products == null ? List.of() : products);
        tags = (tags == null ? List.of() : tags);
        productOrder = (productOrder == null ? List.of() : productOrder);
        tagOption = (tagOption == null ? TagOption.NORMAL : tagOption);
        return this;
    }
}
