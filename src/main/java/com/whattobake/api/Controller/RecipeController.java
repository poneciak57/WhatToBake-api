package com.whattobake.api.Controller;

import com.whattobake.api.Dto.RecipeFilters;
import com.whattobake.api.Dto.RecipeInsertRequest;
import com.whattobake.api.Dto.RecipeUpdateRequest;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/")
    public Flux<Recipe> getAllRecipes(@RequestBody RecipeFilters recipeFilters){
        return recipeService.getAllRecipes(recipeFilters);
    }

    @GetMapping("/{id}")
    public Mono<Recipe> getOneById(@PathVariable("id") Long id){
        return recipeService.getOneById(id);
    }

    @PostMapping("/")
    public Mono<Recipe> newRecipe(@RequestBody RecipeInsertRequest recipeInsertRequest){
        return recipeService.newRecipe(recipeInsertRequest);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRecipe(@PathVariable("id") Long id){
        return recipeService.deleteRecipe(id);
    }

    @PutMapping("/{id}")
    public Mono<Recipe> updateRecipe(@PathVariable("id") Long id, @RequestBody RecipeInsertRequest recipeInsertRequest){
        return recipeService.updateRecipe(RecipeUpdateRequest.builder()
                        .id(id)
                        .title(recipeInsertRequest.getTitle())
                        .image(recipeInsertRequest.getImage())
                        .link(recipeInsertRequest.getLink())
                        .products(recipeInsertRequest.getProducts())
                .build());
    }
}
