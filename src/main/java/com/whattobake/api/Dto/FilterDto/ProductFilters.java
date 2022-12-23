package com.whattobake.api.Dto.FilterDto;

import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Interfaces.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters implements Filter<ProductFilters> {
    private List<ProductOrder> productOrder;

    @Override
    public ProductFilters fillDefaults() {
        if(productOrder == null){
            productOrder = List.of();
        }
        return this;
    }
}
