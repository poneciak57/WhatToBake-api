package com.whattobake.api.Dto.UpdateDto;

import com.whattobake.api.Interfaces.ModelDto;
import com.whattobake.api.Model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagUpdateRequest implements ModelDto<Tag> {
    private Long id;
    private String name;

    @Override
    public Tag toModel() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }
}
