package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.BaseRepositoryHelper;
import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Enum.TagOption;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Util.RecipeCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
public class InfoTest extends BaseRepositoryHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testRecipeInfo_whenFiltersAreDefault_thenReturnCountEqualsToCountAfterFilters() {
        createRecipe(List.of(), List.of());
        createRecipe(List.of(), List.of());
        RecipeFilters filters = RecipeCreator.defaultFilters();
        StepVerifier.create(recipeRepository.info(filters))
                .expectSubscription()
                .expectNext(RecipeInfo.builder()
                        .count(2L)
                        .countWithFilters(2L)
                        .build())
                .verifyComplete();
    }

    @Test
    public void testRecipeInfo_whenTagModeNormalAndTagsProvided_thenReturnLowerCountAfterFilters() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        createRecipe(List.of(), List.of(tag1, tag2));
        createRecipe(List.of(), List.of(tag1));
        RecipeFilters filters = RecipeCreator.defaultFilters();
        filters.setTagOption(TagOption.NORMAL);
        filters.setTags(List.of(tag2.getId()));
        StepVerifier.create(recipeRepository.info(filters))
                .expectSubscription()
                .expectNext(RecipeInfo.builder()
                        .count(2L)
                        .countWithFilters(1L)
                        .build())
                .verifyComplete();
    }

    @Test
    public void testRecipeInfo_whenTagModeStrict_thenReturnLowerCountAfterFilters() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        createRecipe(List.of(), List.of(tag1, tag2));
        createRecipe(List.of(), List.of(tag1));
        createRecipe(List.of(), List.of(tag2));
        RecipeFilters filters = RecipeCreator.defaultFilters();
        filters.setTagOption(TagOption.STRICT);

        filters.setTags(List.of(tag1.getId()));
        StepVerifier.create(recipeRepository.info(filters))
                .expectSubscription()
                .expectNext(RecipeInfo.builder()
                        .count(3L)
                        .countWithFilters(1L)
                        .build())
                .verifyComplete();

        filters.setTags(List.of(tag2.getId()));
        StepVerifier.create(recipeRepository.info(filters))
                .expectSubscription()
                .expectNext(RecipeInfo.builder()
                        .count(3L)
                        .countWithFilters(1L)
                        .build())
                .verifyComplete();
    }

}
