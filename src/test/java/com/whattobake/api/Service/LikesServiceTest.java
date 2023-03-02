package com.whattobake.api.Service;

import com.whattobake.api.Repository.LikeRepository;
import com.whattobake.api.Util.RecipeCreator;
import com.whattobake.api.Util.UserCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

//    Implement all tests

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