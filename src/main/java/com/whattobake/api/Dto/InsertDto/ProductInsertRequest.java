package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Interfaces.InsertRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInsertRequest implements InsertRequestDto<ProductUpdateRequest,Long> {
    private String name;
    private Long category;

    @Override
    public ProductUpdateRequest toUpdateRequest(Long id) {
        return ProductUpdateRequest.builder()
                .id(id)
                .name(name)
                .category(category)
                .build();
    }
}
