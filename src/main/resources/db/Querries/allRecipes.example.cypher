
//Get all recipes query example with manuall mapping
MATCH (recipe:RECIPE)-[rp_rel:NEEDS]->(product:PRODUCT)-[pc_rel:HAS_CATEGORY]->(category:CATEGORY)
CALL {
    WITH recipe
    MATCH (recipe:RECIPE)-[:NEEDS]->(p:PRODUCT)
    WHERE ID(p) IN [3,7]
    RETURN COUNT(p) AS HasProducts
}
RETURN recipe{
    id: ID(recipe),
    .*,
    products: COLLECT(product{
        id: ID(product),
        .*,
        category:category{
            id: ID(category),
            .*}
        })
    }
    ,HasProducts , (COUNT(product) - HasProducts) AS HasNotProducts

// java mapping then
//ObjectMapper mapper = new ObjectMapper();
//return client.query(q)
//       .bind(recipeFilters.getProducts()).to("products")
//.fetchAs(Recipe.class)
//.mappedBy((ts,r)-> mapper.convertValue(r.get("recipe").asMap(), Recipe.class)
//).all();
