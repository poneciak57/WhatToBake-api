package com.whattobake.api.Dto.InsertDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingInsertRequest {

    @NotNull
    @Min(0)
    @Max(5)
    private Long stars;

}
