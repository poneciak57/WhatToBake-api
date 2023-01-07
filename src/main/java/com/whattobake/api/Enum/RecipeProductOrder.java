package com.whattobake.api.Enum;

public enum RecipeProductOrder {
    ALL_ASC(" AllProducts ASC"),
    ALL_DESC(" AllProducts DESC"),
    HAS_ASC(" HasProducts ASC "),
    HAS_DESC(" HasProducts DESC "),
    HASNOT_ASC(" HasNotProducts ASC "),
    HASNOT_DESC(" HasNotProducts DESC "),
    PROGRESS_ASC(" Progress ASC "),
    PROGRESS_DESC(" Progress DESC ");

    private final String value;
    RecipeProductOrder(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
