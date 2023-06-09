package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.RecipeOrder;
import com.whattobake.api.Enum.TagOption;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters implements com.whattobake.api.Interfaces.NotNull<RecipeFilters> {

    @Min(0)
    @Nullable
    private Long page;

    @Min(0)
    @Max(5)
    @Nullable
    private Long rating;

    @Min(0)
    @Nullable
    private Long minProducts;

    @Min(0)
    @Nullable
    private Long maxProducts;

    @Nullable
    private List<@Min(0) @NotNull Long> products;

    @Nullable
    private List<@Min(0) @NotNull Long> keyProducts;

    @Nullable
    private List<@Min(0) @NotNull Long> tags;

    @Nullable
    private List<@Valid RecipeOrder> orderList;

    @Valid
    @Nullable
    private TagOption tagOption;

    public List<RecipeOrder> getOrderList() {
        return Stream.concat(orderList.stream(),Stream.of(RecipeOrder.CREATION_DATE_DESC))
                .distinct().toList();
    }

    @Override
    public RecipeFilters fillDefaults() {
        page = (page == null ? 0 : page);
        rating = (rating == null ? 0 : rating);
        minProducts = (minProducts == null ? 0 : minProducts);
        maxProducts = (maxProducts == null ? Long.MAX_VALUE : maxProducts);
        products = (products == null ? List.of() : products);
        keyProducts = (keyProducts == null ? List.of() : keyProducts);
        tags = (tags == null ? List.of() : tags);
        orderList = (orderList == null ? List.of() : orderList);
        tagOption = (tagOption == null ? TagOption.NORMAL : tagOption);
        return this;
    }
}
