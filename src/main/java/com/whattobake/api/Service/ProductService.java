package com.whattobake.api.Service;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Flux<Product> getAllProducts(ProductFilters productFilters){
        return productRepository.findAll(productFilters);
    }

    public Mono<Product> getOneById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> newProduct(ProductInsertRequest productInsertRequest) {
        return productRepository.create(Map.of(
                "name",productInsertRequest.getName(),
                "category",productInsertRequest.getCategory()
        ));
    }

    public Mono<Product> updateProduct(ProductUpdateRequest productUpdateRequest) {
        return productRepository.update(Map.of(
                "id",productUpdateRequest.getId(),
                "name",productUpdateRequest.getName(),
                "category",productUpdateRequest.getCategory()
        ));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }
}
