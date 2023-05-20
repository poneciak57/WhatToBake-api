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
            rating_count: size([(recipe)<-[rate:RATING]-(:USER) | {}]),
            rating: coalesce(apoc.coll.avg([(recipe)<-[rate:RATING]-(:USER) | rate.stars]), 0),
            products: apoc.coll.sortMaps([ (recipe)-[:NEEDS]->(p:PRODUCT) | p{
                id: ID(p),
                .*,
                category: [(p:PRODUCT)-[:HAS_CATEGORY]->(c:CATEGORY) | c{
                    id: ID(c),
                    .*
                }][0]
            }], "^id"),
            tags:  apoc.coll.sortMaps([ (recipe)-[:HAS_TAG]->(t:TAG) | t{
                id: ID(t),
                .*
            }], "^id")
        }
    """;
    static public final String ROW_NAME = "recipe";

    public RecipeMapper(ReactiveNeo4jClient client) {
        super(client, RETURN,ROW_NAME);
    }


}
