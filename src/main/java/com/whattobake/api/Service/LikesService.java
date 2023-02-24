package com.whattobake.api.Service;

import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikeRepository likeRepository;
    public Mono<Recipe> likeRecipe(Long recipeId, String pbUid) {
        return likeRepository.like(recipeId,pbUid)
                .switchIfEmpty(Mono.error(new NodeNotFound("Recipe with given id: "+recipeId+" does not exist")));
    }

    public Mono<Void> unlikeRecipe(Long recipeId, String pbUid) {
        return likeRepository.unlike(recipeId,pbUid).flatMap(result ->{
            if(!result) return Mono.error(new NodeNotFound("Recipe with given id: "+recipeId+" does not exist"));
            else return Mono.empty();
        });
    }

    public Flux<Recipe> getLikedRecipes(String pbUid) {
        return likeRepository.getRecipes(pbUid);
    }

}
