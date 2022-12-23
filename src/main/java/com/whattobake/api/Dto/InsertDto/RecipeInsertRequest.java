package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Interfaces.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeInsertRequest implements NotNull<RecipeInsertRequest> {
    private String title;
    private String link;
    private String image;
    private List<Long> products;
    private List<Long> tags;

    @Override
    public RecipeInsertRequest fillDefaults() {
        products = (products == null ? List.of() : products);
        tags = (tags == null ? List.of() : tags);
        return this;
    }
}
