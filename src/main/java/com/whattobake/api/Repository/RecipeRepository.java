package com.whattobake.api.Repository;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Model.Recipe;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public interface RecipeRepository extends ReactiveNeo4jRepository<Recipe, Long>{
    Flux<Recipe> findAll(RecipeFilters recipeFilters);
    Mono<Recipe> findOne(Long id);
    Mono<RecipeInfo> info(RecipeFilters recipeFilters);
    Mono<Recipe> create(Map<String, Object> recipe);
    Mono<Recipe> update(Map<String, Object> recipe);
}
