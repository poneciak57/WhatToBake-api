package com.whattobake.api.Dto.UpdateDto;

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
public class CategoryUpdateRequest implements ModelDto<Category> {
    private Long id;
    private String name;
    private String icon;

    @Override
    public Category toModel() {
        return Category.builder()
                .id(id)
                .name(name)
                .icon(icon)
                .build();
    }
}
