package com.whattobake.api.Service;


import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.ProductRepository;
import com.whattobake.api.Util.CategoryCreator;
import com.whattobake.api.Util.ProductCreator;
import com.whattobake.api.Util.TagCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp(){
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(Sort.class))).thenReturn(Flux.just(ProductCreator.valid()));

        Mockito.when(productRepository.findById(TagCreator.VALID_ID)).thenReturn(Mono.just(ProductCreator.valid()));
        Mockito.when(productRepository.findById(TagCreator.INVALID_ID)).thenReturn(Mono.empty());

        Mockito.when(productRepository.save(Product.builder().name(ProductCreator.NAME).category(CategoryCreator.valid()).build())).thenReturn(Mono.just(ProductCreator.valid()));
        Mockito.when(productRepository.save(Product.builder().name(ProductCreator.NAME).build())).thenReturn(Mono.just(ProductCreator.validNoCategory()));
        Mockito.when(productRepository.save(ProductCreator.valid())).thenReturn(Mono.just(ProductCreator.valid()));

        Mockito.when(productRepository.delete(ProductCreator.valid())).thenReturn(Mono.empty());

        Mockito.when(categoryService.getOneById(CategoryCreator.VALID_ID)).thenReturn(Mono.just(CategoryCreator.valid()));
        Mockito.when(categoryService.getOneById(CategoryCreator.INVALID_ID)).thenReturn(Mono.error(new NodeNotFound("test exception")));
    }

    @Test
    @DisplayName("all, when filters are default should return flux of products")
    public void testAllProducts_whenFiltersAreOk_thenReturnFluxOfProducts(){
        StepVerifier.create(productService.getAllProducts(Optional.of(ProductCreator.defaultFilters())))
                .expectSubscription()
                .expectNext(ProductCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("all, when filters are null, should return flux of products")
    public void testAllProducts_whenFiltersAreNull_thenReturnFluxOfProducts(){
        StepVerifier.create(productService.getAllProducts(Optional.empty()))
                .expectSubscription()
                .expectNext(ProductCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should return mono of product")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfProduct(){
        StepVerifier.create(productService.getOneById(ProductCreator.VALID_ID))
                .expectSubscription()
                .expectNext(ProductCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(productService.getOneById(ProductCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("update, should return mono of product")
    public void testUpdateProduct_whenIdIsCorrect_thenReturnMonoOfProduct(){
        StepVerifier.create(productService.updateProduct(ProductCreator.validUpdate()))
                .expectSubscription()
                .expectNext(ProductCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("update, when category id is incorrect, should return mono of product with null category")
    public void testUpdateProduct_whenCategoryIdIsIncorrect_thenReturnMonoOfProductWithCategoryNull(){
        StepVerifier.create(productService.updateProduct(ProductUpdateRequest.builder()
                        .id(ProductCreator.VALID_ID)
                        .name(ProductCreator.NAME)
                        .category(CategoryCreator.INVALID_ID)
                        .build()))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateProduct_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(productService.updateProduct(ProductCreator.invalidUpdate()))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        Mockito.verify(productRepository,Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteProduct_whenIdIsCorrect_thenReturnMonoEmpty(){
        StepVerifier.create(productService.deleteProduct(ProductCreator.VALID_ID))
                .expectSubscription()
                .verifyComplete();
        Mockito.verify(productRepository,Mockito.times(1)).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteProduct_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(productService.deleteProduct(ProductCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        Mockito.verify(productRepository,Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("create, when category id is correct, should return mono of product")
    public void testNewProduct_whenCategoryIdIsCorrect_thenReturnMonoOfProduct(){
        StepVerifier.create(productService.newProduct(ProductCreator.validInsert()))
                .expectSubscription()
                .expectNext(ProductCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("create, when category id is incorrect, should return mono of product")
    public void testNewProduct_whenCategoryIdIsIncorrect_thenReturnMonoOfProductWithCategoryNull(){
        StepVerifier.create(productService.newProduct(ProductCreator.insertWithInvalidCategory()))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

}