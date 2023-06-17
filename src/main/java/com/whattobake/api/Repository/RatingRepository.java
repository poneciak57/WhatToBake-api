package com.whattobake.api.Repository;

import com.whattobake.api.Dto.InsertDto.RatingInsertRequest;
import com.whattobake.api.Model.Recipe;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RatingRepository {

    Mono<Recipe> addRating(RatingInsertRequest rating, Long recipeId, String pbUid);

    Mono<Boolean> deleteRating(Long recipeId, String pbUid);

    Flux<Recipe> ratedRecipes(Long page, String pbUid);
}
