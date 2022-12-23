package com.whattobake.api.Enum;

public enum TagOption {
    STRICT(" WHERE COUNT { (recipe)-[:HAS_TAG]->(tag:TAG) WHERE ID(tag) IN $tags } = $tags_size =  COUNT { (recipe)-[:HAS_TAG]->(:TAG)} "),
    NORMAL(" WHERE COUNT { (recipe)-[:HAS_TAG]->(tag:TAG) WHERE ID(tag) IN $tags } = $tags_size ");

    private final String value;
    TagOption(final String value) {
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
