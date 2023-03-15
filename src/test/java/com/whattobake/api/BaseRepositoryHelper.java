package com.whattobake.api;

import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Repository.CategoryRepository;
import com.whattobake.api.Repository.Implementations.LikeRepositoryImpl;
import com.whattobake.api.Repository.Implementations.RecipeRepositoryImpl;
import com.whattobake.api.Repository.LikeRepository;
import com.whattobake.api.Repository.ProductRepository;
import com.whattobake.api.Repository.RecipeRepository;
import com.whattobake.api.Repository.TagRepository;
import com.whattobake.api.Util.CategoryCreator;
import com.whattobake.api.Util.ProductCreator;
import com.whattobake.api.Util.RecipeCreator;
import com.whattobake.api.Util.TagCreator;
import com.whattobake.api.Util.UserCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;

import java.util.List;

@Import({RecipeRepositoryImpl.class, LikeRepositoryImpl.class})
public abstract class BaseRepositoryHelper extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    protected RecipeRepository recipeRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected LikeRepository likeRepository;

    @Autowired
    protected ReactiveNeo4jClient client;

    protected Recipe prepareRecipeWithEverything() {
        Category c1 = createCategory();
        Product p1 = createProduct(c1);
        Product p2 = createProduct(c1);
        Tag t1 = createTag();
        Recipe recipe = createRecipe(List.of(p1, p2), List.of(t1));
        return likeRecipe(recipe.getId(), UserCreator.VALID_ID);
    }

    protected Recipe likeRecipe(Long recipeId, String uid) {
        return likeRepository.like(recipeId, uid).block();
    }

    protected Recipe createRecipe(List<Product> products, List<Tag> tags) {
        return recipeRepository.save(Recipe.builder()
                        .title(RecipeCreator.TITLE)
                        .link(RecipeCreator.LINK)
                        .image(RecipeCreator.IMAGE)
                        .likes(0L)
                        .tags(tags)
                        .products(products)
                .build()).block();
    }

    protected Product createProduct(Category category) {
        return productRepository.save(Product.builder()
                        .name(ProductCreator.NAME)
                        .category(category)
                .build()).block();
    }

    protected Category createCategory() {
        return categoryRepository.save(Category.builder()
                        .name(CategoryCreator.NAME)
                        .icon(CategoryCreator.ICON)
                .build()).block();
    }

    protected Tag createTag() {
        return tagRepository.save(Tag.builder()
                        .name(TagCreator.NAME)
                .build()).block();
    }

}