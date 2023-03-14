package com.whattobake.api.Repository;

import com.whattobake.api.BaseIntegrationTestEmbeddedDB;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Repository.Implementations.LikeRepositoryImpl;
import com.whattobake.api.Repository.Implementations.RecipeRepositoryImpl;
import com.whattobake.api.Util.RecipeCreator;
import com.whattobake.api.Util.UserCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

@Slf4j
@Import({LikeRepositoryImpl.class, RecipeRepositoryImpl.class})
class LikeRepositoryTest extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ReactiveNeo4jClient client;

    @BeforeEach
    public void setUp() {
        client.query("MATCH (n) DETACH DELETE n;").run().block();
    }

    @Test
    @DisplayName("getRecipes, when user like recipe, should return flux of one recipe")
    public void testGetRecipes_whenUserLikedRecipe_theReturnFluxOfRecipe() {
        Recipe testRecipe = addTestRecipe();
        testRecipe.setLikes(testRecipe.getLikes() + 1);
        client.query("""
               MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
               MERGE (user:USER{pbId:$pbId})-[:LIKES{date:datetime()}]->(recipe)
               RETURN *""")
                .bind(testRecipe.getId()).to("rid")
                .bind(UserCreator.VALID_ID).to("pbId")
                .run().block();
        StepVerifier.create(likeRepository.getRecipes(UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        checkCount(1L,1L,1L);
    }

    @Test
    @DisplayName("like, when recipe and user exist, should return mono of recipe")
    public void testLikeRecipe_whenRecipeAndUserExist_thenReturnMonoOfRecipe() {
        Recipe testRecipe = addTestRecipe();
        testRecipe.setLikes(testRecipe.getLikes() + 1);
        client.query("MERGE (user:USER{pbId:$pbId})").bind(UserCreator.VALID_ID).to("pbId").run().block();
        StepVerifier.create(likeRepository.like(testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        client.query("""
                        MATCH (user:USER{pbId: $pbId})-[like:LIKES]->(recipe:RECIPE)
                        RETURN user.pbId AS uid, COUNT(like) AS likes, ID(recipe) AS recipeId
                        """)
                .bind(UserCreator.VALID_ID).to("pbId")
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(UserCreator.VALID_ID, row.get("uid"));
                    Assertions.assertEquals(testRecipe.getLikes(), row.get("likes"));
                    Assertions.assertEquals(testRecipe.getId(), row.get("recipeId"));
                })
                .verifyComplete();
        checkCount(1L,1L,1L);
    }

    @Test
    @DisplayName("like, when recipe and user exist and also user already liked that recipe, should return mono of recipe")
    public void testLikeRecipe_whenRecipeAndUserExistAndUserAlreadyLikedTheRecipe_thenReturnMonoOfRecipe() {
        Recipe testRecipe = addTestRecipe();
        testRecipe.setLikes(testRecipe.getLikes() + 1);
        client.query("""
               MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
               MERGE (user:USER{pbId:$pbId})-[:LIKES{date:datetime()}]->(recipe)
               RETURN *""")
                .bind(testRecipe.getId()).to("rid")
                .bind(UserCreator.VALID_ID).to("pbId")
                .run().block();
        StepVerifier.create(likeRepository.like(testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        client.query("""
                        MATCH (user:USER{pbId: $pbId})-[like:LIKES]->(recipe:RECIPE)
                        RETURN user.pbId AS uid, COUNT(like) AS likes, ID(recipe) AS recipeId
                        """)
                .bind(UserCreator.VALID_ID).to("pbId")
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(UserCreator.VALID_ID, row.get("uid"));
                    Assertions.assertEquals(testRecipe.getLikes(), row.get("likes"));
                    Assertions.assertEquals(testRecipe.getId(), row.get("recipeId"));
                })
                .verifyComplete();
        checkCount(1L,1L,1L);
    }

    @Test
    @DisplayName("like, when recipe exist and user doesnt, should return mono of recipe")
    public void testLikeRecipe_whenRecipeExistsAndUserDoesnt_thenReturnMonoOfRecipe() {
        Recipe testRecipe = addTestRecipe();
        testRecipe.setLikes(testRecipe.getLikes() + 1);
        StepVerifier.create(likeRepository.like(testRecipe.getId(), UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(testRecipe)
                .verifyComplete();
        client.query("""
                        MATCH (user:USER{pbId: $pbId})-[like:LIKES]->(recipe:RECIPE)
                        RETURN user.pbId AS uid, COUNT(like) AS likes, ID(recipe) AS recipeId
                        """)
                .bind(UserCreator.VALID_ID).to("pbId")
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(UserCreator.VALID_ID, row.get("uid"));
                    Assertions.assertEquals(testRecipe.getLikes(), row.get("likes"));
                    Assertions.assertEquals(testRecipe.getId(), row.get("recipeId"));
                })
                .verifyComplete();
        checkCount(1L,1L,1L);
    }

    @Test
    @DisplayName("like, when recipe and user dont exist, should return mono of recipe")
    public void testLikeRecipe_whenRecipeAndUserDontExist_thenReturnMonoEmpty() {
        StepVerifier.create(likeRepository.like(RecipeCreator.INVALID_ID, UserCreator.VALID_ID))
                .expectSubscription()
                .verifyComplete();
        checkCount(0L, 0L, 0L);
    }

    @Test
    @DisplayName("unlike, when like relationship exists, should return mono of true")
    public void testUnlikeRecipe_whenLikeExists_thenReturnMonoTrue() {
        Recipe testRecipe = addTestRecipe();
        client.query("""
               MATCH (recipe:RECIPE) WHERE ID(recipe) = $rid
               MERGE (user:USER{pbId:$pbId})-[:LIKES{date:datetime()}]->(recipe)
               RETURN *""")
                .bind(testRecipe.getId()).to("rid")
                .bind(UserCreator.VALID_ID).to("pbId")
                .run().block();
        checkCount(1L, 1L, 1L);

        StepVerifier.create(
                likeRepository.unlike(testRecipe.getId(), UserCreator.VALID_ID)
                ).expectSubscription()
                .expectNext(Boolean.TRUE)
                .verifyComplete();
        checkCount(1L, 1L, 0L);
    }

    @Test
    @DisplayName("unlike, when like relationship doesnt exist, should return mono of false")
    public void testUnlikeRecipe_whenLikeDoesntExist_thenReturnMonoOfFalse() {
        StepVerifier.create(
                likeRepository.unlike(RecipeCreator.INVALID_ID, UserCreator.INVALID_ID)
                ).expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        checkCount(0L, 0L, 0L);
    }

    private Recipe addTestRecipe() {
        return recipeRepository.create(Map.of(
                "title", RecipeCreator.TITLE,
                "link", RecipeCreator.LINK,
                "image", RecipeCreator.IMAGE,
                "products", List.of(),
                "tags", List.of()
        )).block();
    }

    private void checkCount(Long recipeCount, Long userCount, Long likesCount) {
        client.query("""
                CALL { MATCH (r:RECIPE) RETURN COUNT(r) as recipeCount }
                CALL { MATCH (u:USER) RETURN COUNT(u) as userCount }
                CALL { MATCH ()-[l:LIKES]->() RETURN COUNT(l) as likesCount }
                RETURN *;
                """)
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(recipeCount, row.get("recipeCount"));
                    Assertions.assertEquals(userCount, row.get("userCount"));
                    Assertions.assertEquals(likesCount, row.get("likesCount"));
                })
                .verifyComplete();
    }
}