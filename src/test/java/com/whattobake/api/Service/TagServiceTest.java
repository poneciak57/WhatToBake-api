package com.whattobake.api.Service;

import com.whattobake.api.Repository.TagRepository;
import com.whattobake.api.Util.TagCreator;
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
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp(){
        Mockito.when(tagRepository.findAll()).thenReturn(Flux.just(TagCreator.validTag()));
        Mockito.when(tagRepository.save(TagCreator.validTag())).thenReturn(Mono.just(TagCreator.validTag()));
        Mockito.when(tagRepository.save(TagCreator.invalidTag())).thenReturn(Mono.empty());
        Mockito.when(tagRepository.findById(TagCreator.VALID_ID)).thenReturn(Mono.just(TagCreator.validTag()));
        Mockito.when(tagRepository.findById(TagCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(tagRepository.delete(TagCreator.validTag())).thenReturn(Mono.empty());
    }



//    allTags Correct

//    oneById Correct

//    oneById Error wrong id

//    updateTag Correct

//    updateTag Error wrong id

//    deleteTag Correct

//    deleteTag Error wrong id

//    newTag correct







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