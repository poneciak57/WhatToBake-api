package com.whattobake.api.Controller;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Dto.InsertDto.RecipeInsertRequest;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Service.RecipeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Tag(name ="1. Recipe")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/info")
    public Mono<RecipeInfo> info(@RequestBody Mono<Optional<RecipeFilters>> recipeFilters){
        return recipeFilters.flatMap(recipeService::info);
    }

    @GetMapping("/")
    public Flux<Recipe> getAllRecipes(@RequestBody Mono<Optional<RecipeFilters>> recipeFilters){
        return recipeFilters.flatMapMany(recipeService::getAllRecipes);
    }

    @GetMapping("/{id}")
    public Mono<Recipe> getOneById(@PathVariable("id") Long id){
        return recipeService.getOneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Recipe> newRecipe(@RequestBody Mono<RecipeInsertRequest> recipeInsertRequest){
        return recipeInsertRequest.flatMap(recipeService::newRecipe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteRecipe(@PathVariable("id") Long id){
        return recipeService.deleteRecipe(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Recipe> updateRecipe(@PathVariable("id") Long id, @RequestBody Mono<RecipeInsertRequest> recipeInsertRequest){
        return recipeInsertRequest.map(r -> r.toUpdateRequest(id)).flatMap(recipeService::updateRecipe);
    }
}
