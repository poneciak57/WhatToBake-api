package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Interfaces.NotNull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters implements NotNull<ProductFilters> {

    @Nullable
    private List<ProductOrder> productOrder;

    @Override
    public ProductFilters fillDefaults() {
        productOrder = (productOrder == null ? List.of() : productOrder);
        return this;
    }
}
