package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.RatingInsertRequest;
import com.whattobake.api.Dto.RecipeDto;
import com.whattobake.api.Security.SecurityHelper;
import com.whattobake.api.Service.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@Tag(name ="6. Rating")
@RestController
@RequestMapping("/api/recipes/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/id")
    public Mono<Map<Long, Long>> ratedRecipeIds(Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMap(u -> ratingService.getRatedShort(u.getPbId()));
    }

    @GetMapping("")
    public Flux<RecipeDto> ratedRecipeList(Mono<Principal> principal, @RequestParam("page") Long page) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMapMany(u -> ratingService.getRated(page, u.getPbId()))
                .map(RecipeDto::fromRecipe);
    }

    @PostMapping("/{recipeId}")
    public Mono<RecipeDto> rate(
            @Valid @RequestBody RatingInsertRequest request,
            @Min(0) @NotNull @PathVariable Long recipeId,
            Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMap(u -> ratingService.addRating(request, recipeId, u.getPbId()))
                .map(RecipeDto::fromRecipe);
    }

    @DeleteMapping("/{recipeId}")
    public Mono<Void> unrate(
            @Min(0) @NotNull @PathVariable Long recipeId,
            Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal)
                .flatMap(u -> ratingService.deleteRating(recipeId, u.getPbId()));
    }
}
