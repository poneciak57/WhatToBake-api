package com.whattobake.api.Service;

import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Repository.RecipeRepository;
import com.whattobake.api.Util.RecipeCreator;
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
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setUp(){
        Mockito.when(recipeRepository.findAll(RecipeCreator.defaultFilters())).thenReturn(Flux.just(RecipeCreator.valid()));
        Mockito.when(recipeRepository.info(RecipeCreator.defaultFilters())).thenReturn(Mono.just(RecipeCreator.validRecipeInfo()));
        Mockito.when(recipeRepository.create(RecipeCreator.validInsertMap())).thenReturn(Mono.just(RecipeCreator.valid()));
        Mockito.when(recipeRepository.update(RecipeCreator.validUpdateMap())).thenReturn(Mono.just(RecipeCreator.valid()));
        Mockito.when(recipeRepository.update(RecipeCreator.invalidUpdateMap())).thenReturn(Mono.empty());
        Mockito.when(recipeRepository.findOne(RecipeCreator.VALID_ID)).thenReturn(Mono.just(RecipeCreator.valid()));
        Mockito.when(recipeRepository.findOne(RecipeCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(recipeRepository.findById(RecipeCreator.VALID_ID)).thenReturn(Mono.just(RecipeCreator.valid()));
        Mockito.when(recipeRepository.findById(RecipeCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(recipeRepository.delete(RecipeCreator.valid())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("all, when filters are default should return flux of ")
    public void testAllRecipes_whenFiltersAreOk_thenReturnFluxOfRecipes(){
        StepVerifier.create(recipeService.getAllRecipes(Optional.of(RecipeCreator.defaultFilters())))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("all, when filters are null, should return flux of recipes")
    public void testAllRecipes_whenFiltersAreNull_thenReturnFluxOfRecipes(){
        StepVerifier.create(recipeService.getAllRecipes(Optional.empty()))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should return mono of recipe")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfRecipe(){
        StepVerifier.create(recipeService.getOneById(RecipeCreator.VALID_ID))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(recipeService.getOneById(RecipeCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("update, should return mono of recipe")
    public void testUpdateRecipe_whenIdIsCorrect_thenReturnMonoOfRecipe(){
        StepVerifier.create(recipeService.updateRecipe(RecipeCreator.validUpdate()))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateRecipe_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(recipeService.updateRecipe(RecipeCreator.invalidUpdate()))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteRecipe_whenIdIsCorrect_thenReturnMonoEmpty(){
        StepVerifier.create(recipeService.deleteRecipe(RecipeCreator.VALID_ID))
                .expectSubscription()
                .verifyComplete();
        Mockito.verify(recipeRepository,Mockito.times(1)).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteRecipe_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(recipeService.deleteRecipe(RecipeCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        Mockito.verify(recipeRepository,Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("create, should return mono of recipe")
    public void testNewRecipe_whenOK_thenReturnMonoOfRecipe(){
        StepVerifier.create(recipeService.newRecipe(RecipeCreator.validInsert()))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
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