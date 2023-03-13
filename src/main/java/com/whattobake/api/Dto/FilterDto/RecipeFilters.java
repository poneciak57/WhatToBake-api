package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Enum.TagOption;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters implements com.whattobake.api.Interfaces.NotNull<RecipeFilters> {

    @Min(0)
    @Nullable
    private Long page;

    @Nullable
    private List<@Min(0) @NotNull Long> products;

    @Nullable
    private List<@Min(0) @NotNull Long> tags;

    @Nullable
    private List<@Valid RecipeProductOrder> productOrder;

    @Valid
    @Nullable
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
