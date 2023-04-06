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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Tag(name ="6. Rating")
@RestController
@RequestMapping("/api/recipe/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

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
