package com.whattobake.api.Util;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Dto.InsertDto.RecipeInsertRequest;
import com.whattobake.api.Dto.UpdateDto.RecipeUpdateRequest;
import com.whattobake.api.Enum.RecipeOrder;
import com.whattobake.api.Enum.TagOption;
import com.whattobake.api.Model.Recipe;

import java.util.List;
import java.util.Map;

public class RecipeCreator {

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 10L;

    public static final String TITLE = "test_title";

    public static final String LINK = "test_link";

    public static final String IMAGE = "test_image";

    public static final Long LIKES = 1L;

    public static Recipe valid(){
        return Recipe.builder()
                .id(VALID_ID)
                .title(TITLE)
                .image(IMAGE)
                .link(LINK)
                .likes(LIKES)
                .tags(List.of(TagCreator.valid()))
                .products(List.of(ProductCreator.valid()))
                .build();
    }

    public static Recipe invalid(){
        return Recipe.builder()
                .id(INVALID_ID)
                .title(TITLE)
                .image(IMAGE)
                .link(LINK)
                .likes(LIKES)
                .tags(List.of(TagCreator.valid()))
                .products(List.of(ProductCreator.valid()))
                .build();
    }

    public static RecipeUpdateRequest validUpdate(){
        return RecipeUpdateRequest.builder()
                .id(VALID_ID)
                .title(TITLE)
                .image(IMAGE)
                .link(LINK)
                .tags(List.of(TagCreator.VALID_ID))
                .products(List.of(ProductCreator.VALID_ID))
                .build();
    }

    public static RecipeUpdateRequest invalidUpdate(){
        return RecipeUpdateRequest.builder()
                .id(INVALID_ID)
                .title(TITLE)
                .image(IMAGE)
                .link(LINK)
                .tags(List.of(TagCreator.VALID_ID))
                .products(List.of(ProductCreator.VALID_ID))
                .build();
    }

    public static RecipeInsertRequest validInsert(){
        return RecipeInsertRequest.builder()
                .title(TITLE)
                .image(IMAGE)
                .link(LINK)
                .tags(List.of(TagCreator.VALID_ID))
                .products(List.of(ProductCreator.VALID_ID))
                .build();
    }

    public static Map<String, Object> validInsertMap(){
        return Map.of(
                "title", TITLE,
                "link", LINK,
                "image", IMAGE,
                "products", List.of(ProductCreator.VALID_ID),
                "tags", List.of(TagCreator.VALID_ID)
        );
    }

    public static Map<String, Object> validUpdateMap(){
        return Map.of(
                "id", VALID_ID,
                "title", TITLE,
                "link", LINK,
                "image", IMAGE,
                "products", List.of(ProductCreator.VALID_ID),
                "tags", List.of(TagCreator.VALID_ID)
        );
    }

    public static Map<String, Object> invalidUpdateMap(){
        return Map.of(
                "id", INVALID_ID,
                "title", TITLE,
                "link", LINK,
                "image", IMAGE,
                "products", List.of(ProductCreator.VALID_ID),
                "tags", List.of(TagCreator.VALID_ID)
        );
    }

    public static RecipeFilters defaultFilters(){
        return (new RecipeFilters()).fillDefaults();
    }

    public static RecipeFilters customFilters(){
        return new RecipeFilters(
                1L,
                List.of(),
                List.of(),
                List.of(RecipeOrder.PRODUCTS_HASNOT_ASC),
                TagOption.STRICT
        );
    }

    public static RecipeInfo validRecipeInfo(){
        return RecipeInfo.builder()
                .count(10L)
                .countWithFilters(2L)
                .build();
    }

}
