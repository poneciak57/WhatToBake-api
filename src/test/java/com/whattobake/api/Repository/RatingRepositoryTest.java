package com.whattobake.api.Repository;

import com.whattobake.api.Dto.InsertDto.RatingInsertRequest;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Util.Creators.RecipeCreator;
import com.whattobake.api.Util.Creators.UserCreator;
import com.whattobake.api.Util.Helpers.BaseRepositoryTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
public class RatingRepositoryTest extends BaseRepositoryTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testRateRecipe_whenRecipeAndUserExist_thenReturnMonoOfRecipe() {
        Recipe testRecipe = createRecipe(List.of(), List.of());
        createTestUser(UserCreator.VALID_ID);
        testRecipe.setLikes(0L);
        testRecipe.setRating((double) 3L);
        StepVerifier.create(ratingRepository.addRating(new RatingInsertRequest(3L), testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        checkCount(1L, 1L, 1L);
    }

    @Test
    public void testRateRecipe_whenRecipeAndUserExistAndThereAreFewRatings_thenReturnMonoOfRecipe() {
        Recipe testRecipe = createRecipe(List.of(), List.of());
        createTestUser("testuser1");
        createTestUser("testuser2");
        rate(2L, testRecipe.getId(), "testuser1");
        testRecipe.setLikes(0L);
        testRecipe.setRating((double) 3L);
        StepVerifier.create(ratingRepository.addRating(new RatingInsertRequest(4L), testRecipe.getId(), "testuser2"))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        checkCount(1L, 2L, 2L);
    }

    @Test
    public void testRateRecipe_whenRecipeExistsAndUserDoesnt_thenReturnMonoOfRecipe() {
        Recipe testRecipe = createRecipe(List.of(), List.of());
        testRecipe.setLikes(0L);
        testRecipe.setRating((double) 3L);
        StepVerifier.create(ratingRepository.addRating(new RatingInsertRequest(3L), testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        checkCount(1L, 1L, 1L);
    }

    @Test
    public void testRateRecipe_whenUserAlreadyRatedRecipe_thenUpdateRatingAndReturnMonoOfRecipe() {
        Recipe testRecipe = createRecipe(List.of(), List.of());
        createTestUser(UserCreator.VALID_ID);
        rate(3L, testRecipe.getId(), UserCreator.VALID_ID);
        testRecipe.setLikes(0L);
        testRecipe.setRating((double) 4L);
        StepVerifier.create(ratingRepository.addRating(new RatingInsertRequest(4L), testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        checkCount(1L, 1L, 1L);
    }

    @Test
    public void testRateRecipe_whenRecipeDoesntExists_thenReturnMonoEmpty() {
        StepVerifier.create(ratingRepository.addRating(new RatingInsertRequest(4L), RecipeCreator.INVALID_ID, UserCreator.INVALID_ID))
                .expectSubscription()
                .verifyComplete();
        checkCount(0L, 0L, 0L);
    }

    @Test
    public void testDeleteRecipeRating_whenRatingExists_thenReturnMonoOfTrue() {
        Recipe testRecipe = createRecipe(List.of(), List.of());
        createTestUser(UserCreator.VALID_ID);
        rate(3L, testRecipe.getId(), UserCreator.VALID_ID);
        StepVerifier.create(ratingRepository.deleteRating(testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        checkCount(1L, 1L, 0L);
    }

    @Test
    public void testDeleteRecipeRating_whenRatingDoesntExist_thenReturnMonoOfFalse() {
        StepVerifier.create(ratingRepository.deleteRating(RecipeCreator.INVALID_ID, UserCreator.INVALID_ID))
                .expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        checkCount(0L, 0L, 0L);
    }

    private void createTestUser(String pbuid) {
        client.query("MERGE (user:USER{pbId:$pbId})").bind(pbuid).to("pbId").run().block();
    }

    private void rate(Long stars, Long recipeId, String pbuid) {
        client.query("""
                MATCH (user:USER{pbId:$pbId})
                MATCH (recipe:RECIPE) WHERE ID(recipe) = $rId
                CREATE (user)-[rate:RATING{stars:$stars}]->(recipe)
                """)
                .bind(pbuid).to("pbId")
                .bind(recipeId).to("rId")
                .bind(stars).to("stars")
                .run().block();
    }

    private void checkCount(Long recipeCount, Long userCount, Long ratingCount) {
        client.query("""
                CALL { MATCH (r:RECIPE) RETURN COUNT(r) as recipeCount }
                CALL { MATCH (u:USER) RETURN COUNT(u) as userCount }
                CALL { MATCH ()-[rate:RATING]->() RETURN COUNT(rate) as ratingCount }
                RETURN *;
                """)
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(recipeCount, row.get("recipeCount"));
                    Assertions.assertEquals(userCount, row.get("userCount"));
                    Assertions.assertEquals(ratingCount, row.get("ratingCount"));
                })
                .verifyComplete();
    }

}
