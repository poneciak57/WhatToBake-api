package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Dto.UpdateDto.RecipeUpdateRequest;
import com.whattobake.api.Interfaces.InsertRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInsertRequest implements com.whattobake.api.Interfaces.NotNull<RecipeInsertRequest>, InsertRequestDto<RecipeUpdateRequest,Long> {
    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String title;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String link;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String image;

    @NotNull
    private List<@Min(0) @NotNull Long> products;

    @NotNull
    private List<@Min(0) @NotNull Long> tags;

    @Override
    public RecipeInsertRequest fillDefaults() {
        products = (products == null ? List.of() : products);
        tags = (tags == null ? List.of() : tags);
        return this;
    }

    @Override
    public RecipeUpdateRequest toUpdateRequest(Long id) {
        return RecipeUpdateRequest.builder()
                .id(id)
                .title(title)
                .link(link)
                .image(image)
                .products(products)
                .tags(tags)
                .build();
    }
}
