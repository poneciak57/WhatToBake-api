package com.whattobake.api.Util;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Model.Tag;

public class TagCreator {

    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 10L;

    public static final String NAME = "test_tag";

    public static Tag valid(){
        return Tag.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }
    public static Tag invalid(){
        return Tag.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }
    public static TagUpdateRequest validUpdate(){
        return TagUpdateRequest.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }

    public static TagUpdateRequest invalidUpdate(){
        return TagUpdateRequest.builder()
                .id(INVALID_ID)
                .name(NAME)
                .build();
    }

    public static TagInsertRequest validInsert(){
        return TagInsertRequest.builder()
                .name(NAME)
                .build();
    }
}
