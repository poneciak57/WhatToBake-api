package com.whattobake.api.Service;

import com.whattobake.api.Dto.TagDto;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Repository.TagRepository;
import com.whattobake.api.Util.Creators.TagCreator;
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
        Mockito.when(tagRepository.findAll()).thenReturn(Flux.just(TagCreator.valid()));
        Mockito.when(tagRepository.save(TagCreator.valid())).thenReturn(Mono.just(TagCreator.valid()));
        Mockito.when(tagRepository.save(TagCreator.validInsert().toModel())).thenReturn(Mono.just(TagCreator.valid()));
//        Mockito.when(tagRepository.save(TagCreator.invalidTag())).thenReturn(Mono.empty());
        Mockito.when(tagRepository.findById(TagCreator.VALID_ID)).thenReturn(Mono.just(TagCreator.valid()));
        Mockito.when(tagRepository.findById(TagCreator.INVALID_ID)).thenReturn(Mono.empty());
        Mockito.when(tagRepository.delete(TagCreator.valid())).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("all, should return flux of tags")
    public void testAllTags_whenIsOk_thenReturnFluxOfTags(){
        StepVerifier.create(tagService.allTags())
                .expectSubscription()
                .expectNext(TagDto.fromTag(TagCreator.valid()))
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should return mono of tag")
    public void testOneById_whenIdIsCorrect_thenReturnMonoOfTag(){
        StepVerifier.create(tagService.oneById(TagCreator.VALID_ID))
                .expectSubscription()
                .expectNext(TagDto.fromTag(TagCreator.valid()))
                .verifyComplete();
    }

    @Test
    @DisplayName("one_by_id, should throw an error")
    public void testOneById_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(tagService.oneById(TagCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
    }

    @Test
    @DisplayName("update, should return mono of tag ")
    public void testUpdateTag_whenIdIsCorrect_thenReturnMonoOfTag(){
        StepVerifier.create(tagService.updateTag(TagCreator.validUpdate()))
                .expectSubscription()
                .expectNext(TagDto.fromTag(TagCreator.valid()))
                .verifyComplete();
    }

    @Test
    @DisplayName("update, should throw an error")
    public void testUpdateTag_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(tagService.updateTag(TagCreator.invalidUpdate()))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        Mockito.verify(tagRepository,Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("delete, should return mono empty")
    public void testDeleteTag_whenIdIsCorrect_thenReturnMonoEmpty(){
        StepVerifier.create(tagService.deleteTag(TagCreator.VALID_ID))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete, should throw an error")
    public void testDeleteTag_whenIdIsIncorrect_thenThrowException(){
        StepVerifier.create(tagService.deleteTag(TagCreator.INVALID_ID))
                .expectSubscription()
                .verifyError(NodeNotFound.class);
        Mockito.verify(tagRepository,Mockito.never()).deleteById(ArgumentMatchers.anyLong());
        Mockito.verify(tagRepository,Mockito.never()).delete(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("create, should return mono of tag")
    public void testNewTag_whenOK_thenReturnMonoOfTag(){
        StepVerifier.create(tagService.newTag(TagCreator.validInsert()))
                .expectSubscription()
                .expectNext(TagDto.fromTag(TagCreator.valid()))
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