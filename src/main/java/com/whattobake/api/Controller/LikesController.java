package com.whattobake.api.Controller;

import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.User;
import com.whattobake.api.Security.SecurityHelper;
import com.whattobake.api.Service.LikesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Tag(name ="5. Recipes likes")
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/all")
    public Flux<Recipe> getLiked(Mono<Principal> principal){
        return principal.map(SecurityHelper::UserFromPrincipal)
                .map(User::getPbId)
                .flatMapMany(likesService::getLikedRecipes);
    }
    @PostMapping("/{recipeId}")
    public Mono<Recipe> like(@PathVariable Long recipeId,Mono<Principal> principal){
        return principal.map(SecurityHelper::UserFromPrincipal).flatMap(u -> likesService.likeRecipe(recipeId,u.getPbId()));
    }

    @DeleteMapping("/{recipeId}")
    public Mono<Void> unlike(@PathVariable Long recipeId,Mono<Principal> principal){
        return principal.map(SecurityHelper::UserFromPrincipal).flatMap(u -> likesService.unlikeRecipe(recipeId,u.getPbId()));
    }
}
