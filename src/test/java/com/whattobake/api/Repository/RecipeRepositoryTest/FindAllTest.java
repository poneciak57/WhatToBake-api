package com.whattobake.api.Repository.RecipeRepositoryTest;

import com.whattobake.api.BaseRepositoryHelper;
import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Enum.TagOption;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Util.RecipeCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

@Slf4j
public class FindAllTest extends BaseRepositoryHelper {


    @DynamicPropertySource
    public static void setRecipePageCountTo3ForTests(DynamicPropertyRegistry registry) {
        registry.add("w2b.recipes.pageCount", () -> 3);
    }

    @BeforeEach
    public void setUp() {
        client.query("MATCH (n) DETACH DELETE n;").run().block();
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
        Category c1 = createCategory();
        Product p1 = createProduct(c1);
        Product p2 = createProduct(c1);
        Tag t1 = createTag();
        Recipe recipe1 = createRecipe(List.of(p1, p2), List.of(t1));
        Recipe recipe2 = createRecipe(List.of(p1, p2), List.of(t1));
        Recipe recipe3 = createRecipe(List.of(p1, p2), List.of(t1));
        Recipe recipe4 = createRecipe(List.of(p1, p2), List.of(t1));
        RecipeFilters filters = RecipeCreator.defaultFilters();

        filters.setPage(0L);
        var l = recipeRepository.findAll(filters).buffer().blockLast();
        log.info(Objects.requireNonNull(Objects.requireNonNull(l).stream().map(Recipe::getId).toList()).toString());
        log.info(recipe1.toString());
        log.info(recipe2.toString());
        log.info(recipe3.toString());
        log.info(recipe4.toString());
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
        filters.setProductOrder(List.of(RecipeProductOrder.PROGRESS_DESC));

        filters.setProducts(List.of(product1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe1, recipe2, recipe3)
                .verifyComplete();

        filters.setProducts(List.of(product2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe2, recipe1, recipe3)
                .verifyComplete();

        filters.setProducts(List.of());
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe1, recipe2, recipe3)
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
        filters.setProductOrder(List.of(RecipeProductOrder.PROGRESS_DESC, RecipeProductOrder.HASNOT_ASC));

        filters.setProducts(List.of(product1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .expectNext(recipe1, recipe3, recipe2)
                .verifyComplete();

        filters.setProducts(List.of(product2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .expectNext(recipe2, recipe1, recipe3)
                .verifyComplete();

        filters.setProducts(List.of());
        StepVerifier.create(recipeRepository.findAll(filters))
                .expectSubscription()
                .expectNext(recipe3, recipe2, recipe1)
                .verifyComplete();
    }

    @Test
    public void testFindAll_whenTagModeDefault_thenReturnFluxOfRecipeInCorrectOrder() {
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
                .expectNext(recipe1, recipe3)
                .verifyComplete();

        filters.setTags(List.of(tag1.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe1, recipe2)
                .verifyComplete();

        filters.setTags(List.of(tag1.getId(), tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe1)
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
    public void testFindAll_whenTagModeStrict_thenReturnFluxOfRecipesWithMatchingTag() {
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
                .expectNext(recipe2)
                .verifyComplete();

        filters.setTags(List.of(tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe3)
                .verifyComplete();

        filters.setTags(List.of(tag1.getId(), tag2.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe1)
                .verifyComplete();

        filters.setTags(List.of());
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .expectNext(recipe4)
                .verifyComplete();

        filters.setTags(List.of(tag3.getId()));
        StepVerifier.create(recipeRepository.findAll(filters).log())
                .expectSubscription()
                .verifyComplete();
    }
}
