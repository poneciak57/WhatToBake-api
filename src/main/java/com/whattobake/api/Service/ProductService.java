package com.whattobake.api.Service;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Exception.NodeNotFound;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    public Flux<Product> getAllProducts(Optional<ProductFilters> productFilters) {
        return productRepository.findAll(Sort.by(
                productFilters.orElse(new ProductFilters()).fillDefaults().getProductOrder().stream()
                        .map(productOrder -> switch (productOrder){
                            case ALPHABETIC_DESC -> Sort.Order.desc("name");
                            case ALPHABETIC_ASC -> Sort.Order.asc("name");
                        }).toList()));
    }

    public Mono<Product> getOneById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Product with given id: "+id+" does not exist")));
    }

    public Mono<Product> newProduct(ProductInsertRequest productInsertRequest) {
        return categoryService.getOneById(productInsertRequest.getCategory())
                .map(c -> Product.builder()
                        .name(productInsertRequest.getName())
                        .category(c)
                        .build())
                .flatMap(productRepository::save);
    }

    public Mono<Product> updateProduct(ProductUpdateRequest productUpdateRequest) {
        return Mono.zip(
                    this.getOneById(productUpdateRequest.getId()),
                    categoryService.getOneById(productUpdateRequest.getCategory())
                ).map(d -> Product.builder()
                        .id(d.getT1().getId())
                        .name(productUpdateRequest.getName())
                        .category(d.getT2())
                        .build())
                .flatMap(productRepository::save);
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NodeNotFound("Product with given id: "+id+" does not exist")))
                .flatMap(productRepository::delete);
    }
}
