package com.whattobake.api.Controller;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Tag(name ="2. Product")
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public Flux<Product> getAllProducts(@RequestBody Optional<ProductFilters> productFilters){
        return productService.getAllProducts(productFilters.orElse(new ProductFilters()).fillDefaults());
    }
    @GetMapping("/{id}")
    public Mono<Product> getOneById(@PathVariable("id") Long id){
        return productService.getOneById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public Mono<Product> newProduct(@RequestBody ProductInsertRequest productInsertRequest){
        return productService.newProduct(productInsertRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable("id") Long id,@RequestBody ProductInsertRequest productInsertRequest){
        return productService.updateProduct(ProductUpdateRequest.builder()
                        .id(id)
                        .name(productInsertRequest.getName())
                        .category(productInsertRequest.getCategory())
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable("id") Long id){
        return productService.deleteProduct(id);
    }

}
