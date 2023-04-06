package com.whattobake.api.Dto;

import com.whattobake.api.Model.Product;

import java.time.Instant;

public record ProductDto(
        Long id,
        String name,
        CategoryDto category,
        Instant creationDate
) {

    public static ProductDto fromProduct(Product product) {
        CategoryDto categoryDto = null;
        if (product.getCategory() != null) {
            categoryDto = CategoryDto.fromCategory(product.getCategory());
        }

        return new ProductDto(
                product.getId(),
                product.getName(),
                categoryDto,
                product.getCreationDate()
        );
    }
}
