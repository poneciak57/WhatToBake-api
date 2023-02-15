package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return tagService.oneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Tag> updateTag(@PathVariable("id") Long id, @RequestBody Mono<TagInsertRequest> tagInsertRequest){
        return tagInsertRequest.map(t -> t.toUpdateRequest(id))
                .flatMap(tagService::updateTag);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Tag> newTag(@RequestBody Mono<TagInsertRequest> tagInsertRequest){
        return tagInsertRequest.flatMap(tagService::newTag);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }
}
