package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.Util.Helpers.BaseRepositoryTestHelper;
import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Enum.RecipeOrder;
import com.whattobake.api.Enum.TagOption;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Util.Creators.RecipeCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
public class FindAllTest extends BaseRepositoryTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @DynamicPropertySource
    public static void setRecipePageCountTo3ForTests(DynamicPropertyRegistry registry) {
        registry.add("w2b.recipes.pageCount", () -> 3);
    }

    @Test
    public void testDefault_whenFiltersAreDefault_thenReturnFluxOfRecipe() {
        Recipe recipe = prepareRecipeWithEverything();
        StepVerifier.create(recipeRepository.findAll(RecipeCreator.defaultFilters()))
                .expectSubscription()
                .expectNext(recipe)
                .verifyComplete();
    }

    @Test
    public void testPaging_whenOtherFiltersAreDefault_thenReturnFluxOfRecipe() {
        prepareRecipeWithEverything();
        prepareRecipeWithEverything();
        prepareRecipeWithEverything();
        prepareRecipeWithEverything();
        RecipeFilters filters = RecipeCreator.defaultFilters();

        filters.setPage(0L);
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNextCount(3L)
                .verifyComplete();

        filters.setPage(1L);
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNextCount(1L)
                .verifyComplete();

        filters.setPage(2L);
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void testFilterByProducts_whenProductOrderIsProgressDesc_thenReturnFluxOfRecipesInRightOrder() {
        Category test_category = createCategory();
        Product product1 = createProduct(test_category);
        Product product2 = createProduct(test_category);
        Recipe recipe1 = createRecipe(List.of(product1, product2), List.of());
        Recipe recipe2 = createRecipe(List.of(product2), List.of());
        Recipe recipe3 = createRecipe(List.of(), List.of());
        RecipeFilters filters = RecipeCreator.defaultFilters();
        filters.setOrderList(List.of(RecipeOrder.PRODUCTS_PROGRESS_DESC));

        filters.setProducts(List.of(product1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .verifyComplete();

        filters.setProducts(List.of(product2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .verifyComplete();

        filters.setProducts(List.of());
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();

    }

    @Test
    public void testFilterByProducts_whenProductOrderIsProgressDescHasNotAsc_thenReturnFluxOfRecipesInRightOrder() {
        Category test_category = createCategory();
        Product product1 = createProduct(test_category);
        Product product2 = createProduct(test_category);
        Recipe recipe1 = createRecipe(List.of(product1, product2), List.of());
        Recipe recipe2 = createRecipe(List.of(product2), List.of());
        Recipe recipe3 = createRecipe(List.of(), List.of());
        RecipeFilters filters = RecipeCreator.defaultFilters();
        filters.setOrderList(List.of(RecipeOrder.PRODUCTS_PROGRESS_DESC, RecipeOrder.PRODUCTS_HASNOT_ASC));

        filters.setProducts(List.of(product1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .verifyComplete();

        filters.setProducts(List.of(product2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .verifyComplete();

        filters.setProducts(List.of());
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();
    }

    @Test
    public void testTagModeDefault_when2Tags4Recipes_thenReturnFluxOfRecipeInCorrectOrder() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        Recipe recipe1 = createRecipe(List.of(), List.of(tag1, tag2));
        Recipe recipe2 = createRecipe(List.of(), List.of(tag1));
        Recipe recipe3 = createRecipe(List.of(), List.of(tag2));
        Recipe recipe4 = createRecipe(List.of(), List.of());
        RecipeFilters filters = RecipeCreator.defaultFilters();

        filters.setTags(List.of(tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();

        filters.setTags(List.of(tag1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();

        filters.setTags(List.of(tag1.getId(), tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();

        filters.setTags(List.of());
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNextCount(3L)
                .verifyComplete();

        filters.setTags(List.of());
        filters.setPage(1L);
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNextCount(1L)
                .verifyComplete();

    }

    @Test
    public void testTagModeStrict_when3Tags4Recipes_thenReturnFluxOfRecipesWithMatchingTag() {
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        Tag tag3 = createTag();
        Recipe recipe1 = createRecipe(List.of(), List.of(tag1, tag2));
        Recipe recipe2 = createRecipe(List.of(), List.of(tag1));
        Recipe recipe3 = createRecipe(List.of(), List.of(tag2));
        Recipe recipe4 = createRecipe(List.of(), List.of());
        RecipeFilters filters = RecipeCreator.defaultFilters();
        filters.setTagOption(TagOption.STRICT);

        filters.setTags(List.of(tag1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe2.getId()))
                .verifyComplete();

        filters.setTags(List.of(tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe3.getId()))
                .verifyComplete();

        filters.setTags(List.of(tag1.getId(), tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe1.getId()))
                .verifyComplete();

        filters.setTags(List.of());
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .consumeNextWith(recipe -> Assertions.assertEquals(recipe.getId(), recipe4.getId()))
                .verifyComplete();

        filters.setTags(List.of(tag3.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .verifyComplete();
    }
}
