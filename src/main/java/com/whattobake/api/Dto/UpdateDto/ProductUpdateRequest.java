package com.whattobake.api.Dto.UpdateDto;

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
public class ProductUpdateRequest {

    @Min(0)
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Min(0)
    @Nullable
    private Long category;
}