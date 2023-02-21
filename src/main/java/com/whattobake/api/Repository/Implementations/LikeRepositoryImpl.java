package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Mapers.RecipeMapper;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final RecipeMapper recipeMapper;
    private final ReactiveNeo4jClient client;

    public Flux<Recipe> getRecipes(String pbUid){
        String q = """
            MATCH (user:USER)-[l:LIKES]->(recipe:RECIPE)
            WHERE user.pbId = $pbId
            RETURN""" + RecipeMapper.RETURN + """
            ORDER BY l.date DESC
            """;
        return recipeMapper.resultAsFlux(recipeMapper.getMapperQueryNoAddon(q), Map.of("pbId",pbUid));
    }

    public Mono<Recipe> like(Long id, String pbUid){
        String q = """
            MATCH (user:USER{pbId: $pbId})
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
            CREATE (user)-[:LIKES{date:datetime()}]->(recipe)
            RETURN""";
        return recipeMapper.resultAsMono(recipeMapper.getMapperQuery(q),Map.of("pbId",pbUid,"rid",id));
    }

    public Mono<Void> unlike(Long id, String pbUid){
        String q = """
            MATCH (user:USER{pbId: $pbId})
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
            MATCH (user)-[l:LIKES]->(recipe)
            DELETE l;
        """;
        return client.query(q).bindAll(Map.of("pbId",pbUid,"rid",id)).fetchAs(Void.class).first();
    }
}