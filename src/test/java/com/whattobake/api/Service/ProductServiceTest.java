package com.whattobake.api.Service;


import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Repository.ProductRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp(){
        Mockito.when(productRepository.findAll(ArgumentMatchers.any(ProductFilters.class))).thenReturn(Flux.just(ProductCreator.valid()));
        Mockito.when(productRepository.create(ProductCreator.validInsertMap())).thenReturn(Mono.just(ProductCreator.valid()));
        Mockito.when(productRepository.update(ProductCreator.validUpdateMap())).thenReturn(Mono.just(ProductCreator.valid()));
        Mockito.when(productRepository.update(ProductCreator.invalidUpdateMap())).thenReturn(Mono.empty());
        Mockito.when(productRepository.findById(TagCreator.VALID_ID)).thenReturn(Mono.just(ProductCreator.valid()));
        Mockito.when(productRepository.findById(TagCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(productRepository.delete(ProductCreator.valid())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("all, when filters are default should return flux of products")
    public void testAllProducts_whenFiltersAreOk_thenReturnFluxOfProducts(){

    }

    @Test
    @DisplayName("all, when filters are null, should return flux of products")
    public void testAllProducts_whenFiltersAreNull_thenReturnFluxOfProducts(){

    }

    @Test
    @DisplayName("one_by_id, should return mono of product")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfProduct(){

    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("update, should return mono of product")
    public void testUpdateProduct_whenIdIsCorrect_thenReturnMonoOfProduct(){

    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateProduct_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteProduct_whenIdIsCorrect_thenReturnMonoEmpty(){

    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteProduct_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("create, should return mono of product")
    public void testNewProduct_whenOK_thenReturnMonoOfProduct(){

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