package com.whattobake.api.Controller;

import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name ="3. Category")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public Flux<Category> allCategories() {
        return categoryService.allCategories();
    }

    @GetMapping("/{id}")
    public Mono<Category> oneById(@Min (0) @NotNull @PathVariable("id") Long id) {
        return categoryService.getOneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public Mono<Category> newCategory(@Valid @RequestBody Mono<CategoryInsertRequest> categoryInsertRequest) {
        return categoryInsertRequest.flatMap(categoryService::newCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Category> updateCategory(
            @Min(0) @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody Mono<CategoryInsertRequest> categoryInsertRequest) {
        return categoryInsertRequest.map(c -> c.toUpdateRequest(id))
                .flatMap(categoryService::updateCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCategory(@Min(0) @NotNull @PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }

}
