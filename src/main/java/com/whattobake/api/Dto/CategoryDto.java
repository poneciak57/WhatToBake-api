package com.whattobake.api.Dto;

import com.whattobake.api.Model.Category;

public record CategoryDto(
        Long id,
        String name,
        String icon
) {

    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getIcon()
        );
    }
}
