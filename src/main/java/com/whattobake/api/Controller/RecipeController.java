package com.whattobake.api.Controller;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Dto.InsertDto.RecipeInsertRequest;
import com.whattobake.api.Exception.RecipeNotFoundException;
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
        return recipeFilters.map(r -> r.orElse(new RecipeFilters()).fillDefaults()).flatMap(recipeService::info);
    }

    @GetMapping("/")
    public Flux<Recipe> getAllRecipes(@RequestBody Mono<Optional<RecipeFilters>> recipeFilters){
        return recipeFilters.map(r -> r.orElse(new RecipeFilters()).fillDefaults()).flatMapMany(recipeService::getAllRecipes);
    }

    @GetMapping("/{id}")
    public Mono<Recipe> getOneById(@PathVariable("id") Mono<Long> id){
        return id.flatMap(recipeService::getOneById)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe with given id: "+id+" does not exist")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Recipe> newRecipe(@RequestBody Mono<RecipeInsertRequest> recipeInsertRequest){
        return recipeInsertRequest.flatMap(recipeService::newRecipe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteRecipe(@PathVariable("id") Mono<Long> id){
        return id.flatMap(recipeService::deleteRecipe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Recipe> updateRecipe(@PathVariable("id") Mono<Long> id, @RequestBody Mono<RecipeInsertRequest> recipeInsertRequest){
        return Mono.zip(id,recipeInsertRequest)
                .map(data -> data.getT2().toUpdateRequest(data.getT1()))
                .flatMap(recipeService::updateRecipe)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException("Recipe with given id: "+id+" does not exist")));
    }
}
