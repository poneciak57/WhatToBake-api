package com.whattobake.api.Enum;

public enum ProductOrder {
    ALPHABETIC_ASC(" p.name ASC "),
    ALPHABETIC_DESC(" p.name DESC ");

    private final String value;
    ProductOrder(final String value) {
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
