package com.whattobake.api.Dto;

import com.whattobake.api.Model.Category;

import java.time.Instant;

public record CategoryDto(
        Long id,
        String name,
        String icon,
        Instant creationDate
) {

    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getCreationDate()
        );
    }
}
