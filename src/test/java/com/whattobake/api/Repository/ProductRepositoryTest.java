package com.whattobake.api.Repository;

import com.whattobake.api.Util.Helpers.BaseIntegrationTestEmbeddedDB;
import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.Implementations.ProductRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Import(ProductRepositoryImpl.class)
class ProductRepositoryTest extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll().block();
        categoryRepository.deleteAll().block();
    }

    @Test
    @DisplayName("all, when filters are alphabetic asc, should return flux of products in correct order")
    public void testFindAll_whenFilterIsAlphabeticASC_thenReturnsFluxOfProductsInCorrectOrder() {
        productRepository.saveAll(List.of(
                Product.builder().name("a_product").build(),
                Product.builder().name("b_product").build()
        )).log().blockLast();
        productRepository.findAll(new ProductFilters(List.of(ProductOrder.ALPHABETIC_ASC)))
                .as(StepVerifier::create)
                .consumeNextWith(p -> Assertions.assertEquals("a_product",p.getName()))
                .consumeNextWith(p -> Assertions.assertEquals("b_product",p.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("all, when filters are alphabetic desc, should return flux of products in correct order")
    public void testFindAll_whenFilterIsAlphabeticDESC_thenReturnsFluxOfProductsInCorrectOrder() {
        productRepository.saveAll(List.of(
                Product.builder().name("a_product").build(),
                Product.builder().name("b_product").build()
        )).log().blockLast();
        productRepository.findAll(new ProductFilters(List.of(ProductOrder.ALPHABETIC_DESC)))
                .as(StepVerifier::create)
                .consumeNextWith(p -> Assertions.assertEquals("b_product",p.getName()))
                .consumeNextWith(p -> Assertions.assertEquals("a_product",p.getName()))
                .verifyComplete();
    }

    @Test
    @DisplayName("update, when updating name, should return mono of product")
    public void testUpdate_whenUpdatingName_thenReturnsMonoOfProduct() {
        Category category = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name1").icon("example_icon1").build()
                )).block();
        productRepository.save(
                        Product.builder().name("example_product").category(category).build()
                ).flatMap(p -> productRepository.update(Map.of(
                        "id", p.getId(),
                        "name", "example_product_updated",
                        "category", p.getCategory().getId()
                ))).as(StepVerifier::create)
                .consumeNextWith(p -> {
                    Assertions.assertEquals(p.getName(), "example_product_updated");
                    Assertions.assertEquals(p.getCategory(), category);
                }).verifyComplete();
        productRepository.count().as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(c,1))
                .verifyComplete();
    }

    @Test
    @DisplayName("update, when updating category, should return mono of product")
    public void testUpdate_whenUpdatingCategory_thenReturnsMonoOfProduct() {
        Category category1 = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name1").icon("example_icon1").build()
                )).block();
        Category category2 = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name2").icon("example_icon2").build()
                )).block();
        productRepository.save(
                        Product.builder().name("example_product").category(category1).build()
                ).flatMap(p -> productRepository.update(Map.of(
                        "id", p.getId(),
                        "name", p.getName(),
                        "category", Objects.requireNonNull(category2).getId()
                ))).as(StepVerifier::create)
                .consumeNextWith(p -> {
                    Assertions.assertEquals(p.getName(), "example_product");
                    Assertions.assertEquals(p.getCategory(), category2);
                }).verifyComplete();
        productRepository.count().as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(c,1))
                .verifyComplete();
    }

    @Test
    @DisplayName("update, when product does not exist, should return mono empty")
    public void testUpdate_whenProductDoesntExist_thenReturnsMonoEmpty() {
        Category category = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name1").icon("example_icon1").build()
                )).block();
        productRepository.update(Map.of(
                        "id", 10L,
                        "name", "dummy_name",
                        "category", Objects.requireNonNull(category).getId()
                )).as(StepVerifier::create)
                .verifyComplete();
        productRepository.count().as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(c,0))
                .verifyComplete();
    }

    @Test
    @DisplayName("update, when category does not exist, should return mono of product with category equals null")
    public void testUpdate_whenCategoryDoesntExist_thenReturnsMonoEmpty() {
        Category category = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name1").icon("example_icon1").build()
                )).block();
        productRepository.save(
                        Product.builder().name("example_product").category(category).build()
                ).flatMap(p -> productRepository.update(Map.of(
                        "id", p.getId(),
                        "name", "example_product_updated",
                        "category", 10L
                ))).as(StepVerifier::create)
                .consumeNextWith(p -> {
                    Assertions.assertEquals(p.getName(), "example_product_updated");
                    Assertions.assertNull(p.getCategory());
                }).verifyComplete();
        productRepository.count().as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(c,1))
                .verifyComplete();
    }

    @Test
    @DisplayName("create, when creating product with name and category, should return mono of product")
    public void testCreate_whenDataIsValid_thenReturnsMonoOfProduct(){
        Category category = categoryRepository.deleteAll()
                .then(categoryRepository.save(
                        Category.builder().name("example_name1").icon("example_icon1").build()
                )).block();
        productRepository.create(Map.of(
                        "name", "example_name",
                        "category", Objects.requireNonNull(category).getId()
                )).flatMap(p-> productRepository.findById(p.getId()))
                .as(StepVerifier::create)
                .consumeNextWith(p -> {
                    Assertions.assertEquals(p.getName(),"example_name");
                    Assertions.assertEquals(p.getCategory(), category);
                    Assertions.assertNotNull(p.getId());
                }).verifyComplete();
    }

    @Test
    @DisplayName("create, when category id is incorrect, should return mono of product with category equals null")
    public void testCreate_whenCategoryIdIsIncorrect_thenReturnsMonoOfProduct(){
        productRepository.create(Map.of(
                        "name", "example_name",
                        "category", 10L
                )).flatMap(p-> productRepository.findById(p.getId()))
                .as(StepVerifier::create)
                .consumeNextWith(p -> {
                    Assertions.assertEquals(p.getName(),"example_name");
                    Assertions.assertNull(p.getCategory());
                    Assertions.assertNotNull(p.getId());
                }).verifyComplete();
    }

}