package com.whattobake.api.Service;

import com.whattobake.api.Repository.TagRepository;
import com.whattobake.api.Util.TagCreator;
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
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp(){
        Mockito.when(tagRepository.findAll()).thenReturn(Flux.just(TagCreator.validTag()));
        Mockito.when(tagRepository.save(TagCreator.validTag())).thenReturn(Mono.just(TagCreator.validTag()));
//        Mockito.when(tagRepository.save(TagCreator.invalidTag())).thenReturn(Mono.empty());
        Mockito.when(tagRepository.findById(TagCreator.VALID_ID)).thenReturn(Mono.just(TagCreator.validTag()));
        Mockito.when(tagRepository.findById(TagCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(tagRepository.delete(TagCreator.validTag())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("all, should return flux of tags")
    public void testAllTags_whenIsOk_thenReturnFluxOfTags(){

    }

    @Test
    @DisplayName("one_by_id, should return mono of tag")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfTag(){

    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("update, should return mono of tag ")
    public void testUpdateTag_whenIdIsCorrect_thenReturnMonoOfTag(){

    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateTag_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteTag_whenIdIsCorrect_thenReturnMonoEmpty(){

    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteTag_whenIdIsIncorrect_thenThrowException(){

    }

    @Test
    @DisplayName("create, should return mono of tag")
    public void testNewTag_whenOK_thenReturnMonoOfTag(){

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