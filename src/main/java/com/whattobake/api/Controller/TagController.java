package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.TagDto;
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
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@Validated
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    public Flux<TagDto> allTags() {
        return tagService.allTags().map(TagDto::fromTag);
    }

    @GetMapping("/{id}")
    public Mono<TagDto> oneById(@Min(0) @NotNull @PathVariable("id") Long id) {
        return tagService.oneById(id).map(TagDto::fromTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<TagDto> updateTag(
            @Min(0) @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody Mono<TagInsertRequest> tagInsertRequest) {
        return tagInsertRequest.map(t -> t.toUpdateRequest(id))
                .flatMap(tagService::updateTag).map(TagDto::fromTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TagDto> newTag(@Valid @RequestBody Mono<TagInsertRequest> tagInsertRequest) {
        return tagInsertRequest.flatMap(tagService::newTag).map(TagDto::fromTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTag(@Min(0) @NotNull @PathVariable("id") Long id) {
        return tagService.deleteTag(id);
    }

}
