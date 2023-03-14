package com.whattobake.api;

import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Recipe;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Repository.*;
import com.whattobake.api.Repository.Implementations.LikeRepositoryImpl;
import com.whattobake.api.Repository.Implementations.RecipeRepositoryImpl;
import com.whattobake.api.Util.CategoryCreator;
import com.whattobake.api.Util.ProductCreator;
import com.whattobake.api.Util.RecipeCreator;
import com.whattobake.api.Util.TagCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import({RecipeRepositoryImpl.class, LikeRepositoryImpl.class})
public abstract class BaseRepositoryHelper extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private LikeRepository likeRepository;

    private void likeRecipe(Long recipeId, String uid) {
        likeRepository.like(recipeId, uid).block();
    }

    private Recipe createRecipe(List<Product> products, List<Tag> tags) {
        return recipeRepository.save(Recipe.builder()
                        .title(RecipeCreator.TITLE)
                        .link(RecipeCreator.LINK)
                        .image(RecipeCreator.IMAGE)
                        .likes(0L)
                        .tags(tags)
                        .products(products)
                .build()).block();
    }

    private Product createProduct(Category category) {
        return productRepository.save(Product.builder()
                        .name(ProductCreator.NAME)
                        .category(category)
                .build()).block();
    }

    private Category createCategory() {
        return categoryRepository.save(Category.builder()
                        .name(CategoryCreator.NAME)
                        .icon(CategoryCreator.ICON)
                .build()).block();
    }

    private Tag createTag() {
        return tagRepository.save(Tag.builder()
                        .name(TagCreator.NAME)
                .build()).block();
    }

}