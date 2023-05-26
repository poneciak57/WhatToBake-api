package com.whattobake.api.Controller;

import com.whattobake.api.Dto.RecipeDto;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.User;
import com.whattobake.api.Security.SecurityHelper;
import com.whattobake.api.Service.LikesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Tag(name ="5. Recipes likes")
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/id")
    public Flux<Long> getLikedIds(Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .map(User::getPbId)
                .flatMapMany(likesService::getLikedRecipes)
                .map(Recipe::getId);
    }

    @GetMapping("")
    public Flux<RecipeDto> getLiked(Mono<Principal> principal, @RequestParam("page") Long page) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .map(User::getPbId)
                .flatMapMany(uid -> likesService.getLikedRecipesPages(uid, page))
                .map(RecipeDto::fromRecipe);
    }

    @PostMapping("/{recipeId}")
    public Mono<RecipeDto> like(
            @Min(0) @NotNull @PathVariable Long recipeId,
            Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMap(u -> likesService.likeRecipe(recipeId, u.getPbId()))
                .map(RecipeDto::fromRecipe);
    }

    @DeleteMapping("/{recipeId}")
    public Mono<Void> unlike(
            @Min(0) @NotNull @PathVariable Long recipeId,
            Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMap(u -> likesService.unlikeRecipe(recipeId, u.getPbId()));
    }
}
