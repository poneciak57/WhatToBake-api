package com.whattobake.api.Service;

import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Dto.UpdateDto.CategoryUpdateRequest;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Flux<Category> allCategories(){
        return categoryRepository.findAll();
    }

    public Mono<Category> getOneById(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Category with given id: "+id+" does not exist")));
    }

    public Mono<Category> newCategory(CategoryInsertRequest categoryInsertRequest) {
        return categoryRepository.save(categoryInsertRequest.toModel());
    }

    public Mono<Category> updateCategory(CategoryUpdateRequest categoryUpdateRequest) {
        return categoryRepository
                .findById(categoryUpdateRequest.getId())
                .switchIfEmpty(Mono.error(new NodeNotFound("Category with given id: "+categoryUpdateRequest.getId()+" does not exist")))
                .flatMap(e -> categoryRepository.save(categoryUpdateRequest.toModel()));
    }

    public Mono<Void> deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Category with given id: "+id+" does not exist")))
                .flatMap(categoryRepository::delete);
    }
}
