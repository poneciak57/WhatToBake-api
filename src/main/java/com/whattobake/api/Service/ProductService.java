package com.whattobake.api.Service;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.CategoryRepository;
import com.whattobake.api.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Flux<Product> getAllProducts(Optional<ProductFilters> productFilters){
        return productRepository.findAll(productFilters.orElse(new ProductFilters()).fillDefaults());
    }

    public Mono<Product> getOneById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Product with given id: "+id+" does not exist")));
    }

    public Mono<Product> newProduct(ProductInsertRequest productInsertRequest) {
        return categoryRepository.findById(productInsertRequest.getCategory())
                .map(c -> Product.builder()
                        .name(productInsertRequest.getName())
                        .category(c)
                        .build())
                .defaultIfEmpty(Product.builder().name(productInsertRequest.getName()).build())
                .flatMap(productRepository::save);
    }

    public Mono<Product> updateProduct(ProductUpdateRequest productUpdateRequest) {
        return productRepository.update(Map.of(
                "id",productUpdateRequest.getId(),
                "name",productUpdateRequest.getName(),
                "category",productUpdateRequest.getCategory()
        )).switchIfEmpty(Mono.error(new NodeNotFound("Product with given id: "+productUpdateRequest.getId()+" does not exist")));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Product with given id: "+id+" does not exist")))
                .flatMap(productRepository::delete);
    }
}
