package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Interfaces.InsertRequestDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInsertRequest implements InsertRequestDto<ProductUpdateRequest,Long> {

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Min(0)
    @Nullable
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
