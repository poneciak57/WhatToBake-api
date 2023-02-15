package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Exception.CategoryNotFoundException;
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
    public Mono<Category> getOneById(@PathVariable("id") Long id){
        return categoryService.getOneById(id)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with given id: "+id+" does not exist")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Category> newProduct(@RequestBody Mono<CategoryInsertRequest> categoryInsertRequest){
        return categoryInsertRequest.flatMap(categoryService::newCategory);
    }

//    Update category
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Category> updateCategory(@PathVariable("id") Long id,@RequestBody Mono<CategoryInsertRequest> categoryInsertRequest) {
        return categoryInsertRequest.map(c -> c.toUpdateRequest(id))
                .flatMap(categoryService::updateCategory)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException("Category with given id: "+id+" does not exist")));
    }
//    Delete category
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCategory(@PathVariable("id") Long id){
        return categoryService.deleteCategory(id);
    }

}
