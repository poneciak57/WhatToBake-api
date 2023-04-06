package com.whattobake.api.Dto;

import com.whattobake.api.Model.Recipe;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record RecipeDto(
        Long id,
        String title,
        String link,
        String image,
        Long likes,
        List<ProductDto> products,
        List<TagDto> tags,
        Instant creationDate
) {

    public static RecipeDto fromRecipe(Recipe recipe) {
        List<ProductDto> productDtos = null;
        if (recipe.getProducts() != null) {
            productDtos = recipe.getProducts()
                    .stream()
                    .map(ProductDto::fromProduct)
                    .collect(Collectors.toList());
        }

        List<TagDto> tagDtos = null;
        if (recipe.getTags() != null) {
            tagDtos = recipe.getTags()
                    .stream()
                    .map(TagDto::fromTag)
                    .collect(Collectors.toList());
        }

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getLink(),
                recipe.getImage(),
                recipe.getLikes(),
                productDtos,
                tagDtos,
                recipe.getCreationDate()
        );
    }
}