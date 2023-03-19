package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.Util.Helpers.BaseRepositoryTestHelper;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Util.Creators.RecipeCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@Slf4j
public class FindOneByIdTest extends BaseRepositoryTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testFindOne_whenRecipeExists_thenReturnMonoOfRecipe() {
        Recipe recipe = prepareRecipeWithEverything();
        StepVerifier.create(recipeRepository.findOne(recipe.getId()))
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
    }

    @Test
    public void testFindOne_whenRecipeDoesntExist_thenReturnMonoEmpty() {
        StepVerifier.create(recipeRepository.findOne(RecipeCreator.INVALID_ID))
                .expectSubscription()
                .verifyComplete();
    }

}
