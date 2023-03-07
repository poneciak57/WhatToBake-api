package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.BaseIntegrationTestEmbeddedDB;
import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;


@Import(ProductRepositoryImpl.class)
class ProductRepositoryImplTest extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        productRepository.deleteAll().block();
    }

    @Test
    public void testFindAll_whenFilterIsAlphabeticASC_thenReturnsFluxOfProductsInCorrectOrder(){
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
    public void testFindAll_whenFilterIsAlphabeticDESC_thenReturnsFluxOfProductsInCorrectOrder(){
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
}