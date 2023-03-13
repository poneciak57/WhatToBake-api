package com.whattobake.api.Dto.UpdateDto;

import com.whattobake.api.Interfaces.ModelDto;
import com.whattobake.api.Model.Tag;
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
public class TagUpdateRequest implements ModelDto<Tag> {

    @Min(0)
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Override
    public Tag toModel() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }

}
