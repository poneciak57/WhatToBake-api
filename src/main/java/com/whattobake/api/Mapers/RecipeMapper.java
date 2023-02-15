package com.whattobake.api.Mapers;

import com.whattobake.api.Model.Recipe;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper extends Neo4jResultMapper<Recipe>{
    static public final String RETURN = """
        recipe{
            id: ID(recipe),
            .*,
            likes: size([(recipe)<-[l:LIKES]-(:USER) | {}]),
            products: [ (recipe)-[:NEEDS]->(p:PRODUCT)-[:HAS_CATEGORY]->(c:CATEGORY) | p{
                id: ID(p),
                .*,
                category: c{
                    id: ID(c),
                    .*
             }}],
            tags: [ (recipe)-[:HAS_TAG]->(t:TAG) | t{
                id: ID(t),
                .*
            }]
        }
    """;
    static public final String ROW_NAME = "recipe";

    public RecipeMapper(ReactiveNeo4jClient client) {
        super(client, RETURN,ROW_NAME);
    }


}
