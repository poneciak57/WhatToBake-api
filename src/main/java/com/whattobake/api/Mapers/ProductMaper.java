package com.whattobake.api.Mapers;

import com.whattobake.api.Model.Product;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;

public class ProductMaper extends Neo4jResultMapper<Product> {

    static public final String RETURN = """
        product{
            id: ID(product),
            .*,
            category: [(p)-[:HAS_CATEGORY]-(c:CATEGORY) | c{
                id: ID(c)
                .*
            }][0]
        }
    """;
    static public final String ROW_NAME = "product";
    public ProductMaper(ReactiveNeo4jClient client) {
        super(client, RETURN,ROW_NAME);
    }
}
