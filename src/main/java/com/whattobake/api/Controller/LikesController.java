package com.whattobake.api.Controller;

import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Service.LikesService;
import com.whattobake.api.Service.RecipeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name ="5. Recipes likes")
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final RecipeService recipeService;

    @GetMapping("/all")
    public Flux<Recipe> getLiked(){
//        TODO get liked recipes
        return null;
    }
    @PostMapping("/{recipeId}")
    public Mono<Recipe> like(@PathVariable Long recipeId){
//        TODO like recipe
        return null;
    }

    @DeleteMapping("/{recipeId}")
    public Mono<Recipe> unlike(@PathVariable Long recipeId){
//        TODO unlike recipe
        return null;
    }
}
