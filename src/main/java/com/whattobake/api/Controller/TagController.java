package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@io.swagger.v3.oas.annotations.tags.Tag(name ="4. Tag")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    public Flux<Tag> allTags() {
        return tagService.allTags();
    }

    @GetMapping("/{id}")
    public Mono<Tag> oneById(@Min(0) @NotNull @PathVariable("id") Long id) {
        return tagService.oneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Tag> updateTag(
            @Min(0) @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody Mono<TagInsertRequest> tagInsertRequest) {
        return tagInsertRequest.map(t -> t.toUpdateRequest(id))
                .flatMap(tagService::updateTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Tag> newTag(@Valid @RequestBody Mono<TagInsertRequest> tagInsertRequest) {
        return tagInsertRequest.flatMap(tagService::newTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTag(@Min(0) @NotNull @PathVariable("id") Long id) {
        return tagService.deleteTag(id);
    }

}
