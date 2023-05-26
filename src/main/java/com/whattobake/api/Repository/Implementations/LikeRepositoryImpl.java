package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Mapers.RecipeMapper;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    @Value("${w2b.recipes.pageCount}")
    private Long RECIPES_PER_PAGE;

    private final RecipeMapper recipeMapper;

    private final ReactiveNeo4jClient client;

    public Flux<Recipe> getRecipes(String pbUid) {
        String q = """
            MATCH (user:USER{pbId:$pbId})-[l:LIKES]->(recipe:RECIPE)
            RETURN""" + RecipeMapper.RETURN + """
            ORDER BY l.date DESC
            """;
        return recipeMapper.resultAsFlux(recipeMapper.getMapperQueryNoAddon(q), Map.of("pbId",pbUid));
    }

    public Mono<Recipe> like(Long id, String pbUid) {
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
            MERGE (user:USER{pbId:$pbId})
            WITH user, recipe
            MERGE (user)-[like:LIKES]->(recipe)
            ON CREATE
            SET like.date = datetime()
            RETURN""";
        return recipeMapper.resultAsMono(recipeMapper.getMapperQuery(q),Map.of("pbId",pbUid,"rid",id));
    }

    public Mono<Boolean> unlike(Long id, String pbUid) {
        String q = """
            MATCH (user:USER{pbId: $pbId})
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
            MATCH (user)-[l:LIKES]->(recipe)
            DELETE l;
        """;
        return client.query(q).bindAll(Map.of("pbId",pbUid,"rid",id)).run()
                .map(resultSummary -> resultSummary.counters().relationshipsDeleted() != 0);
    }

    @Override
    public Flux<Recipe> getRecipesPages(String pbUid, Long page) {
        String q = """
            MATCH (user:USER{pbId:$pbId})-[l:LIKES]->(recipe:RECIPE)
            RETURN""" + RecipeMapper.RETURN + """
            ORDER BY l.date DESC
            SKIP""" + " " + RECIPES_PER_PAGE * page + " LIMIT " + RECIPES_PER_PAGE;
        return recipeMapper.resultAsFlux(recipeMapper.getMapperQueryNoAddon(q), Map.of("pbId",pbUid));
    }
}
