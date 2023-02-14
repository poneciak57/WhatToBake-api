package com.whattobake.api.Service;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    public Flux<Tag> allTags() {
        return tagRepository.findAll();
    }
    public Mono<Tag> oneById(Long id){
        return tagRepository.findById(id);
    }

    public Mono<Tag> updateTag(TagUpdateRequest tagUpdateRequest){
        return tagRepository.save(tagUpdateRequest.toModel());
    }
    public Mono<Void> deleteTag(Long id){
        return tagRepository.deleteById(id);
    }
    public Mono<Tag> newTag(TagInsertRequest tagInsertRequest){
        return tagRepository.save(tagInsertRequest.toModel());
    }
}
