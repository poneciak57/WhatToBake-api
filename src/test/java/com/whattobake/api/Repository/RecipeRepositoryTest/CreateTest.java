package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.Util.Helpers.BaseRepositoryTestHelper;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Util.Creators.ProductCreator;
import com.whattobake.api.Util.Creators.RecipeCreator;
import com.whattobake.api.Util.Creators.TagCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class CreateTest extends BaseRepositoryTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testCreate_whenRecipeDoesntHaveProductsAndTags_thenReturnMonoOfRecipe() {
        recipeRepository.create(
                Map.of(
                        "title", RecipeCreator.TITLE,
                        "link", RecipeCreator.LINK,
                        "image", RecipeCreator.IMAGE,
                        "products", List.of(),
                        "tags", List.of()
                )).map(Recipe::getId).flatMap(recipeRepository::findById)
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(recipe -> {
                    Assertions.assertEquals(Objects.requireNonNull(recipe).getTitle(), RecipeCreator.TITLE);
                    Assertions.assertEquals(recipe.getLink(), RecipeCreator.LINK);
                    Assertions.assertEquals(recipe.getImage(), RecipeCreator.IMAGE);
                    Assertions.assertEquals(recipe.getProducts(), List.of());
                    Assertions.assertEquals(recipe.getTags(), List.of());
                })
                .verifyComplete();
        checkCount(1L, 0L, 0L, 0L);
    }

    @Test
    public void testCreate_whenProductsAndTagsAreSupplied_thenReturnMonoOfRecipe() {
        Category category = createCategory();
        Product product = createProduct(category);
        Tag tag = createTag();
        recipeRepository.create(
                        Map.of(
                                "title", RecipeCreator.TITLE,
                                "link", RecipeCreator.LINK,
                                "image", RecipeCreator.IMAGE,
                                "products", List.of(product.getId()),
                                "tags", List.of(tag.getId())
                        )).map(Recipe::getId).flatMap(recipeRepository::findById)
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(recipe -> {
                    Assertions.assertEquals(Objects.requireNonNull(recipe).getTitle(), RecipeCreator.TITLE);
                    Assertions.assertEquals(recipe.getLink(), RecipeCreator.LINK);
                    Assertions.assertEquals(recipe.getImage(), RecipeCreator.IMAGE);
                    Assertions.assertEquals(recipe.getProducts(), List.of(product));
                    Assertions.assertEquals(recipe.getTags(), List.of(tag));
                })
                .verifyComplete();
        checkCount(1L, 1L, 1L, 1L);
    }

    @Test
    public void testCreate_whenProductAndTagDontExistInDB_thenReturnMonoOfRecipeWithoutProducts() {
        recipeRepository.create(
                        Map.of(
                                "title", RecipeCreator.TITLE,
                                "link", RecipeCreator.LINK,
                                "image", RecipeCreator.IMAGE,
                                "products", List.of(ProductCreator.INVALID_ID),
                                "tags", List.of(TagCreator.INVALID_ID)
                        )).map(Recipe::getId).flatMap(recipeRepository::findById)
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(recipe -> {
                    Assertions.assertEquals(Objects.requireNonNull(recipe).getTitle(), RecipeCreator.TITLE);
                    Assertions.assertEquals(recipe.getLink(), RecipeCreator.LINK);
                    Assertions.assertEquals(recipe.getImage(), RecipeCreator.IMAGE);
                    Assertions.assertEquals(recipe.getProducts(), List.of());
                    Assertions.assertEquals(recipe.getTags(), List.of());
                })
                .verifyComplete();
        checkCount(1L, 0L, 0L, 0L);
    }

    @Test
    public void testCreate_whenRecipeAlreadyExists_thenReturnMonoOfExistingRecipe() {
        Recipe recipe = createRecipe(List.of(), List.of());
        StepVerifier.create(recipeRepository.create(Map.of(
                        "title", recipe.getTitle(),
                        "link", recipe.getLink(),
                        "image", recipe.getImage(),
                        "products", List.of(),
                        "tags", List.of()
                )))
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
        checkCount(1L, 0L, 0L, 0L);
    }

    private void checkCount(Long recipeCount, Long tagCount, Long productCount, Long categoryCount) {
        client.query("""
                CALL { MATCH (r:RECIPE) RETURN COUNT(r) as recipeCount }
                CALL { MATCH (p:PRODUCT) RETURN COUNT(p) as productCount }
                CALL { MATCH (t:TAG) RETURN COUNT(t) as tagCount }
                CALL { MATCH (c:CATEGORY) RETURN COUNT(c) as categoryCount }
                RETURN *;
                """)
                .fetch().first()
                .as(StepVerifier::create)
                .expectSubscription()
                .consumeNextWith(row -> {
                    Assertions.assertEquals(recipeCount, row.get("recipeCount"));
                    Assertions.assertEquals(tagCount, row.get("tagCount"));
                    Assertions.assertEquals(productCount, row.get("productCount"));
                    Assertions.assertEquals(categoryCount, row.get("categoryCount"));
                })
                .verifyComplete();
    }
}
