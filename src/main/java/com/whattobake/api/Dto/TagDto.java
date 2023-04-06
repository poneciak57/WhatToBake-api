package com.whattobake.api.Dto;

import com.whattobake.api.Model.Tag;

public record TagDto(
        Long id,
        String name
) {

    public static TagDto fromTag(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName()
        );
    }
}
