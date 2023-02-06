package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Exception.TagNotFoundException;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@io.swagger.v3.oas.annotations.tags.Tag(name ="4. Tag")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/")
    public Flux<Tag> allTags(){
        return tagService.allTags();
    }

    @GetMapping("/{id}")
    public Mono<Tag> oneById(@PathVariable("id") Long id){
        return tagService.oneById(id)
                .switchIfEmpty(Mono.error(new TagNotFoundException("Tag with given id: "+ id + " does not exist")));
    }

    @PutMapping("/{id}")
    public Mono<Tag> updateTag(@PathVariable("id") Long id, @RequestBody TagInsertRequest tagInsertRequest){
        return tagService.updateTag(TagUpdateRequest.builder()
                .id(id)
                .name(tagInsertRequest.getName())
                .build())
                .switchIfEmpty(Mono.error(new TagNotFoundException("Tag with given id: "+ id + " does not exist")));
    }
    @PostMapping("/")
    public Mono<Tag> newTag(@RequestBody TagInsertRequest tagInsertRequest){
        return tagService.newTag(tagInsertRequest);
    }
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }
}
