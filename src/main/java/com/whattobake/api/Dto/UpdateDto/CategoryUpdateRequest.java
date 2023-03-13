package com.whattobake.api.Dto.UpdateDto;

import com.whattobake.api.Interfaces.ModelDto;
import com.whattobake.api.Model.Category;
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
public class CategoryUpdateRequest implements ModelDto<Category> {

    @Min(0)
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
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
