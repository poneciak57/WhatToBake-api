package com.whattobake.api.Mapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RecipeMaper {

    private final ReactiveNeo4jClient client;
    static public final String RETURN = """
        recipe{
            id: ID(recipe),
            .*,
            likes:0,
            products: [ (recipe)-[:NEEDS]->(p:PRODUCT)-[:HAS_CATEGORY]->(c:CATEGORY) | p{
                id: ID(p),
                .*,
                category: c{
                    id: ID(c),
                    .*
             }}]
        }
    """;

    public Mono<Recipe> resultAsRecipe(String query, Map<String,Object> params){
        ObjectMapper mapper = new ObjectMapper();
        return client.query(query)
                .bindAll(params)
                .fetchAs(Recipe.class)
                .mappedBy((ts,r)-> mapper.convertValue(r.get("recipe").asMap(), Recipe.class)
                ).first();
    }
    public Flux<Recipe> resultAsRecipes(String query, Map<String,Object> params){
        ObjectMapper mapper = new ObjectMapper();
        return client.query(query)
                .bindAll(params)
                .fetchAs(Recipe.class)
                .mappedBy((ts,r)-> mapper.convertValue(r.get("recipe").asMap(), Recipe.class)
                ).all();
    }
}
