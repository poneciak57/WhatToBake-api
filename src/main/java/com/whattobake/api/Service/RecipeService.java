package com.whattobake.api.Service;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Dto.InsertDto.RecipeInsertRequest;
import com.whattobake.api.Dto.UpdateDto.RecipeUpdateRequest;
import com.whattobake.api.Exception.RecipeNotFoundException;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    public Mono<RecipeInfo> info(Optional<RecipeFilters> recipeFilters){
        return recipeRepository.info(recipeFilters.orElse(new RecipeFilters()).fillDefaults());
    }

    public Flux<Recipe> getAllRecipes(Optional<RecipeFilters> recipeFilters){
        return recipeRepository.findAll(recipeFilters.orElse(new RecipeFilters()).fillDefaults());
    }
    public Mono<Recipe> getOneById(Long id) {
        return recipeRepository.findOne(id)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe with given id: "+id+" does not exist")));
    }

    public Flux<Recipe> getLikedRecipes(String pbUid){
        return recipeRepository.getLikedRecipes(pbUid);
    }

    public Mono<Recipe> updateRecipe(RecipeUpdateRequest recipeUpdateRequest){
        return recipeRepository.update(Map.of(
                "id", recipeUpdateRequest.getId(),
                "title", recipeUpdateRequest.getTitle(),
                "link", recipeUpdateRequest.getLink(),
                "image", recipeUpdateRequest.getImage(),
                "products", recipeUpdateRequest.getProducts(),
                "tags", recipeUpdateRequest.getTags()))
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe with given id: "+recipeUpdateRequest.getId()+" does not exist")));
    }
    public Mono<Void> deleteRecipe(Long id){
        return recipeRepository.deleteById(id);
    }
    public Mono<Recipe> newRecipe(RecipeInsertRequest recipeInsertRequest){
        return recipeRepository.create(Map.of(
                "title", recipeInsertRequest.getTitle(),
                "link", recipeInsertRequest.getLink(),
                "image", recipeInsertRequest.getImage(),
                "products", recipeInsertRequest.getProducts(),
                "tags", recipeInsertRequest.getTags()
        ));
    }
}
