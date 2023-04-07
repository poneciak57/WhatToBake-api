package com.whattobake.api.Enum;

public enum RecipeOrder {
    PRODUCTS_ALL_ASC(" AllProducts ASC"),
    PRODUCTS_ALL_DESC(" AllProducts DESC"),
    PRODUCTS_HAS_ASC(" HasProducts ASC "),
    PRODUCTS_HAS_DESC(" HasProducts DESC "),
    PRODUCTS_HASNOT_ASC(" HasNotProducts ASC "),
    PRODUCTS_HASNOT_DESC(" HasNotProducts DESC "),
    PRODUCTS_PROGRESS_ASC(" Progress ASC "),
    PRODUCTS_PROGRESS_DESC(" Progress DESC "),

    CREATION_DATE_ASC(" recipe.creation_date ASC "),
    CREATION_DATE_DESC(" recipe.creation_date DESC "),

    RATING_ASC("recipe.rating ASC "),
    RATING_DESC("recipe.rating DESC ");


    private final String value;
    RecipeOrder(final String value) {
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
