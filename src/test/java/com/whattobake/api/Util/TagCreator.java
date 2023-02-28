package com.whattobake.api.Util;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Model.Tag;

public class TagCreator {

    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = 10L;

    public static final String NAME = "test_tag";

    public static Tag validTag(){
        return Tag.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }
    public static Tag invalidTag(){
        return Tag.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }
    public static TagUpdateRequest validTagUpdateRequest(){
        return TagUpdateRequest.builder()
                .id(VALID_ID)
                .name(NAME)
                .build();
    }

    public static TagUpdateRequest invalidTagUpdateRequest(){
        return TagUpdateRequest.builder()
                .id(INVALID_ID)
                .name(NAME)
                .build();
    }

    public static TagInsertRequest validTagInsertRequest(){
        return TagInsertRequest.builder()
                .name(NAME)
                .build();
    }
}
