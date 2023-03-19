package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.BaseRepositoryHelper;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Util.RecipeCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

@Slf4j
public class UpdateTest extends BaseRepositoryHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testUpdate_whenNoProductsAndTagsAreProvided_thenReturnMonoOfRecipe() {
        Recipe recipe = createRecipe(List.of(), List.of());
        recipe.setTitle("updated_test_title");
        recipe.setLink("updated_test_link");
        recipe.setImage("updated_test_image");
        recipeRepository.update(
                        Map.of(
                                "id", recipe.getId(),
                                "title", "updated_test_title",
                                "link", "updated_test_link",
                                "image", "updated_test_image",
                                "products", List.of(),
                                "tags", List.of()
                        ))
                .as(StepVerifier::create)
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
        checkCount(1L, 0L, 0L, 0L);
    }

    @Test
    public void testUpdate_whenRecipeHasProductAndItsChange_thenReturnMonoOfRecipe() {
        Category category = createCategory();
        Product product1 = createProduct(category);
        Product product2 = createProduct(category);
        Recipe recipe = createRecipe(List.of(product1), List.of());
        recipe.setTitle("updated_test_title");
        recipe.setLink("updated_test_link");
        recipe.setImage("updated_test_image");
        recipe.setProducts(List.of(product2));
        recipeRepository.update(
                        Map.of(
                                "id", recipe.getId(),
                                "title", "updated_test_title",
                                "link", "updated_test_link",
                                "image", "updated_test_image",
                                "products", List.of(product2.getId()),
                                "tags", List.of()
                        ))
                .as(StepVerifier::create)
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
        checkCount(1L, 0L, 2L, 1L);
    }

    @Test
    public void testUpdate_whenRecipeHasTagAndItsChange_thenReturnMonoOfRecipe() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        Recipe recipe = createRecipe(List.of(), List.of(tag1));
        recipe.setTitle("updated_test_title");
        recipe.setLink("updated_test_link");
        recipe.setImage("updated_test_image");
        recipe.setTags(List.of(tag2));
        recipeRepository.update(
                        Map.of(
                                "id", recipe.getId(),
                                "title", "updated_test_title",
                                "link", "updated_test_link",
                                "image", "updated_test_image",
                                "products", List.of(),
                                "tags", List.of(tag2.getId())
                        ))
                .as(StepVerifier::create)
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
        checkCount(1L, 2L, 0L, 0L);
    }

    @Test
    public void testUpdate_whenRecipeDoesntExist_thenReturnMonoEmpty() {
        recipeRepository.update(
                        Map.of(
                                "id", RecipeCreator.INVALID_ID,
                                "title", "updated_test_title",
                                "link", "updated_test_link",
                                "image", "updated_test_image",
                                "products", List.of(),
                                "tags", List.of()
                        ))
                .as(StepVerifier::create)
                .expectSubscription()
                .verifyComplete();
        checkCount(0L, 0L, 0L, 0L);
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
