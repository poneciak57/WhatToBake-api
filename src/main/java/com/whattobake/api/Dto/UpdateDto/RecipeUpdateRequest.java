package com.whattobake.api.Dto.UpdateDto;

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
public class RecipeUpdateRequest {

    @Min(0)
    @NotNull
    private Long id;

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
}
