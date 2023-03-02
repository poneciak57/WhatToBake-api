package com.whattobake.api.Util;

import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Dto.UpdateDto.CategoryUpdateRequest;
import com.whattobake.api.Model.Category;

public class CategoryCreator {

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 10L;

    public static final String NAME = "test_category";

    public static final String ICON = "test_icon";

    public static Category valid(){
        return Category.builder()
                .id(VALID_ID)
                .icon(ICON)
                .name(NAME)
                .build();
    }

    public static Category invalid(){
        return Category.builder()
                .id(INVALID_ID)
                .icon(ICON)
                .name(NAME)
                .build();
    }

    public static CategoryUpdateRequest validUpdate(){
        return CategoryUpdateRequest.builder()
                .id(VALID_ID)
                .name(NAME)
                .icon(ICON)
                .build();
    }

    public static CategoryUpdateRequest invalidUpdate(){
        return CategoryUpdateRequest.builder()
                .id(INVALID_ID)
                .name(NAME)
                .icon(ICON)
                .build();
    }

    public static CategoryInsertRequest validInsert(){
        return CategoryInsertRequest.builder()
                .name(NAME)
                .icon(ICON)
                .build();
    }

}
