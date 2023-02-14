package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Exception.ProductNotFoundException;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name ="3. Category")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public Flux<Category> allCategories(){
        return categoryService.allCategories();
    }

    @GetMapping("/{id}")
    public Mono<Category> getOneById(@PathVariable("id") Mono<Long> id){
        return id.flatMap(categoryService::getOneById)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Recipe with given id: "+id+" does not exist")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Category> newProduct(@RequestBody Mono<CategoryInsertRequest> categoryInsertRequest){
        return categoryInsertRequest.flatMap(categoryService::newCategory);
    }

//    Update category
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Category> updateCategory(@PathVariable("id") Mono<Long> id,@RequestBody Mono<CategoryInsertRequest> categoryInsertRequest) {
        return Mono.zip(id,categoryInsertRequest)
                .map(data -> data.getT2().toUpdateRequest(data.getT1()))
                .flatMap(categoryService::updateCategory);
    }
//    Delete category
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCategory(@PathVariable("id") Mono<Long> id){
        return id.flatMap(categoryService::deleteCategory);
    }

}
