package com.whattobake.api.Enum;

public enum ProductOrder {
    @SuppressWarnings("unused") ALPHABETIC_ASC(" product.name ASC "),
    @SuppressWarnings("unused") ALPHABETIC_DESC(" product.name DESC ");

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
