package com.whattobake.api.Repository;

import com.whattobake.api.Model.Recipe;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LikeRepository {

    Flux<Recipe> getRecipes(String pbUid);

    Mono<Recipe> like(Long recipeId, String pbUid);

    Mono<Boolean> unlike(Long recipeId, String pbUid);

    Flux<Recipe> getRecipesPages(String pbUid, Integer page);
}
