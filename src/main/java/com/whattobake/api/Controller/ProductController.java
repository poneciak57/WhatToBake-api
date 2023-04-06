package com.whattobake.api.Controller;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.ProductDto;
import com.whattobake.api.Service.ProductService;
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

import java.util.Optional;

@Tag(name ="2. Product")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public Flux<ProductDto> getAllProducts(@Valid @RequestBody Mono<Optional<ProductFilters>> productFilters) {
        return productFilters.flatMapMany(productService::getAllProducts).map(ProductDto::fromProduct);
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> oneById(@Min(0) @NotNull @PathVariable("id") Long id) {
        return productService.getOneById(id).map(ProductDto::fromProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public Mono<ProductDto> newProduct(@Valid @RequestBody Mono<ProductInsertRequest> productInsertRequest) {
        return productInsertRequest.flatMap(productService::newProduct).map(ProductDto::fromProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(
            @Min(0) @NotNull @PathVariable("id") Long id,
            @Valid @RequestBody Mono<ProductInsertRequest> productInsertRequest) {
        return productInsertRequest.map(p->p.toUpdateRequest(id))
                .flatMap(productService::updateProduct).map(ProductDto::fromProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@Min(0) @NotNull @PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }

}
