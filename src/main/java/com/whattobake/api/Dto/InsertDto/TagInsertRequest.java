package com.whattobake.api.Dto.InsertDto;

import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Interfaces.InsertRequestDto;
import com.whattobake.api.Interfaces.ModelDto;
import com.whattobake.api.Model.Tag;
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
public class TagInsertRequest implements ModelDto<Tag>, InsertRequestDto<TagUpdateRequest,Long> {

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Override
    public TagUpdateRequest toUpdateRequest(Long id) {
        return TagUpdateRequest.builder()
                .id(id)
                .name(name)
                .build();
    }

    @Override
    public Tag toModel() {
        return Tag.builder()
                .name(name)
                .build();
    }

}
