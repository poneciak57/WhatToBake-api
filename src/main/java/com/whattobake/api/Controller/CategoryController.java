package com.whattobake.api.Controller;

import com.whattobake.api.Dto.CategoryDto;
import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name ="3. Category")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public Flux<CategoryDto> allCategories() {
        return categoryService.allCategories();
    }

    @GetMapping("/{id}")
    public Mono<CategoryDto> oneById(@Min (0) @NotNull @PathVariable("id") Long id) {
        return categoryService.getOneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public Mono<CategoryDto> newCategory(@Valid @RequestBody Mono<CategoryInsertRequest> categoryInsertRequest) {
        return categoryInsertRequest.flatMap(categoryService::newCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<CategoryDto> updateCategory(
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
