package com.whattobake.api.Service;

import com.whattobake.api.Repository.CategoryRepository;
import com.whattobake.api.Util.CategoryCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp(){
        Mockito.when(categoryRepository.findAll()).thenReturn(Flux.just(CategoryCreator.valid()));
        Mockito.when(categoryRepository.save(CategoryCreator.valid())).thenReturn(Mono.just(CategoryCreator.valid()));
        Mockito.when(categoryRepository.findById(CategoryCreator.VALID_ID)).thenReturn(Mono.just(CategoryCreator.valid()));
        Mockito.when(categoryRepository.findById(CategoryCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(categoryRepository.delete(CategoryCreator.valid())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("all, should return flux of categories")
    public void testAllCategories_whenIsOk_thenReturnFluxOfCategories(){

    }

    @Test
    @DisplayName("one_by_id, should return mono of category")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfCategory(){

    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("update, should return mono of category")
    public void testUpdateCategory_whenIdIsCorrect_thenReturnMonoOfCategory(){

    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateCategory_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteCategory_whenIdIsCorrect_thenReturnMonoEmpty(){

    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteCategory_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("create, should return mono of category")
    public void testNewCategorie_whenOk_thenReturnMonoOfCategory(){

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