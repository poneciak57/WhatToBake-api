package com.whattobake.api.Service;

import com.whattobake.api.Dto.InsertDto.TagInsertRequest;
import com.whattobake.api.Dto.UpdateDto.TagUpdateRequest;
import com.whattobake.api.Model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {

    private final ReactiveNeo4jTemplate template;
    public Flux<Tag> allTags() {
        return template.findAll(Tag.class);
    }
    public Mono<Tag> oneById(Long id){
        return template.findById(id,Tag.class);
    }

    public Mono<Tag> updateTag(TagUpdateRequest tagUpdateRequest){
        return template.save(Tag.builder()
                .id(tagUpdateRequest.getId())
                .name(tagUpdateRequest.getName())
                .build());
    }
    public Mono<Void> deleteTag(Long id){
        return template.deleteById(id,Tag.class);
    }
    public Mono<Tag> newTag(TagInsertRequest tagInsertRequest){
        return template.save(Tag.builder()
                .name(tagInsertRequest.getName())
                .build());
    }
}
