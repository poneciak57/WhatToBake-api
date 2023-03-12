package com.whattobake.api.Util;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Model.Product;

import java.util.List;
import java.util.Map;

public class ProductCreator {

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 10L;

    public static final String NAME = "test_product";

    public static Product valid() {
        return Product.builder()
                .id(VALID_ID)
                .name(NAME)
                .category(CategoryCreator.valid())
                .build();
    }

    public static ProductUpdateRequest validUpdate() {
        return ProductUpdateRequest.builder()
                .id(VALID_ID)
                .name(NAME)
                .category(CategoryCreator.VALID_ID)
                .build();
    }

    public static ProductUpdateRequest invalidUpdate() {
        return ProductUpdateRequest.builder()
                .id(INVALID_ID)
                .name(NAME)
                .category(CategoryCreator.VALID_ID)
                .build();
    }

    public static ProductInsertRequest validInsert() {
        return ProductInsertRequest.builder()
                .name(NAME)
                .category(CategoryCreator.VALID_ID)
                .build();
    }

    public static ProductInsertRequest insertWithInvalidCategory() {
        return ProductInsertRequest.builder()
                .name(NAME)
                .category(CategoryCreator.INVALID_ID)
                .build();
    }

    public static Map<String, Object> validInsertMap() {
        return Map.of(
                "name", NAME,
                "category",CategoryCreator.VALID_ID
        );
    }

    public static Map<String, Object> validUpdateMap() {
        return Map.of(
                "id", VALID_ID,
                "name", NAME,
                "category", CategoryCreator.VALID_ID
        );
    }

    public static Map<String, Object> invalidUpdateMap() {
        return Map.of(
                "id", INVALID_ID,
                "name", NAME,
                "category", CategoryCreator.VALID_ID
        );
    }

    public static ProductFilters defaultFilters() {
        return (new ProductFilters()).fillDefaults();
    }

    public static ProductFilters customFilters() {
        return new ProductFilters(List.of(ProductOrder.ALPHABETIC_DESC));
    }
}
