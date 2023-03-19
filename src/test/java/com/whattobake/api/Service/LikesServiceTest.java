package com.whattobake.api.Service;

import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Repository.LikeRepository;
import com.whattobake.api.Util.Creators.RecipeCreator;
import com.whattobake.api.Util.Creators.UserCreator;
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
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


@ExtendWith(SpringExtension.class)
class LikesServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikesService likesService;

    @BeforeEach
    public void setUp(){
        Mockito.when(likeRepository.like(RecipeCreator.VALID_ID, UserCreator.VALID_ID)).thenReturn(Mono.just(RecipeCreator.valid()));
        Mockito.when(likeRepository.like(RecipeCreator.INVALID_ID,UserCreator.VALID_ID)).thenReturn(Mono.empty());
        Mockito.when(likeRepository.like(RecipeCreator.VALID_ID,UserCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(likeRepository.unlike(RecipeCreator.VALID_ID,UserCreator.VALID_ID)).thenReturn(Mono.just(true));
        Mockito.when(likeRepository.unlike(RecipeCreator.INVALID_ID,UserCreator.VALID_ID)).thenReturn(Mono.just(false));
        Mockito.when(likeRepository.unlike(RecipeCreator.VALID_ID,UserCreator.INVALID_ID)).thenReturn(Mono.just(false));
        Mockito.when(likeRepository.getRecipes(UserCreator.VALID_ID)).thenReturn(Flux.just(RecipeCreator.valid()));
    }

    @Test
    @DisplayName("like, when recipe id is correct, should return mono of recipe")
    public void testLikeRecipe_whenRecipeIdIsCorrect_thenReturnsMonoOfRecipe(){
        StepVerifier.create(likesService.likeRecipe(RecipeCreator.VALID_ID,UserCreator.VALID_ID))
                .expectSubscription()
                .expectNext(RecipeCreator.valid())
                .verifyComplete();
    }

    @Test
    @DisplayName("like, when recipe id is incorrect, should throw NodeNotFoundException")
    public void testLikeRecipe_whenRecipeIdIsIncorrect_thenThrowException(){
        StepVerifier.create(likesService.likeRecipe(RecipeCreator.INVALID_ID,UserCreator.VALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("unlike, when recipe-user relation exists, should return mono empty")
    public void testUnlikeRecipe_whenRelationExists_thenReturnMonoEmpty(){
        StepVerifier.create(likesService.unlikeRecipe(RecipeCreator.VALID_ID,UserCreator.VALID_ID))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("unlike, when recipe-user relation doesn't exists, should throw NodeNotFoundException")
    public void testUnlikeRecipe_whenRelationDoesNotExists_thenThrowException(){
        StepVerifier.create(likesService.unlikeRecipe(RecipeCreator.INVALID_ID,UserCreator.VALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        StepVerifier.create(likesService.unlikeRecipe(RecipeCreator.VALID_ID,UserCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("getLiked, should return flux of recipes")
    public void testGetLikedRecipes_whenOk_thenReturnFluxOfRecipes(){
        StepVerifier.create(likesService.getLikedRecipes(UserCreator.VALID_ID))
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