package com.whattobake.api.Dto;

import com.whattobake.api.Model.Tag;

import java.time.Instant;

public record TagDto(
        Long id,
        String name,
        Instant creationDate
) {

    public static TagDto fromTag(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName(),
                tag.getCreationDate()
        );
    }
}
