package com.whattobake.api.Service;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.TagDto;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Flux<TagDto> allTags() {
        return tagRepository.findAll()
                .map(TagDto::fromTag);
    }

    public Mono<TagDto> oneById(Long id) {
        return tagRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Tag with given id: "+ id + " does not exist")))
                .map(TagDto::fromTag);
    }

    public Mono<TagDto> updateTag(TagUpdateRequest tagUpdateRequest) {
        return tagRepository
                .findById(tagUpdateRequest.getId())
                .switchIfEmpty(Mono.error(new NodeNotFound("Tag with given id: "+ tagUpdateRequest.getId() + " does not exist")))
                .flatMap(e -> tagRepository.save(tagUpdateRequest.toModel()))
                .map(TagDto::fromTag);
    }

    public Mono<Void> deleteTag(Long id) {
        return tagRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Tag with given id: "+ id + " does not exist")))
                .flatMap(tagRepository::delete);
    }

    public Mono<TagDto> newTag(TagInsertRequest tagInsertRequest) {
        return tagRepository.save(tagInsertRequest.toModel())
                .map(TagDto::fromTag);
    }
}
