package com.whattobake.api.Enum;

public enum RecipeProductOrder {
    MOST(" HasProducts DESC "),
    LEAST(" HasNotProducts ASC");

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
