package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Dto.UpdateDto.CategoryUpdateRequest;
import com.whattobake.api.Interfaces.InsertRequestDto;
import com.whattobake.api.Interfaces.ModelDto;
import com.whattobake.api.Model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryInsertRequest implements ModelDto<Category>, InsertRequestDto<CategoryUpdateRequest,Long> {
    private String name;
    private String icon;

    @Override
    public Category toModel() {
        return Category.builder()
                .name(name)
                .icon(icon)
                .build();
    }
    @Override
    public CategoryUpdateRequest toUpdateRequest(Long id) {
        return CategoryUpdateRequest.builder()
                .id(id)
                .name(name)
                .icon(icon)
                .build();
    }
}
