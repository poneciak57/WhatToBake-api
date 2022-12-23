package com.whattobake.api.Service;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Dto.InsertDto.ProductInsertRequest;
import com.whattobake.api.Dto.UpdateDto.ProductUpdateRequest;
import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

//    private final ReactiveNeo4jClient client;
    private final ReactiveNeo4jTemplate template;
//    private final ProductRepository productRepository;

    public Flux<Product> getAllProducts(ProductFilters productFilters){
        String q = """
            MATCH (p:PRODUCT)-[hc:HAS_CATEGORY]->(c:CATEGORY)
            RETURN *
        """;
        if(!productFilters.getProductOrder().isEmpty()){
            q +=" ORDER BY " + productFilters.getProductOrder().stream()
                    .map(ProductOrder::getValue)
                    .collect(Collectors.joining(","));
        }
        return template.findAll(q, Map.of(

        ),Product.class);
    }

    public Mono<Product> getOneById(Long id) {
        return null;
    }

    public Mono<Product> newProduct(ProductInsertRequest productInsertRequest) {
        return null;
    }

    public Mono<Product> updateProduct(ProductUpdateRequest build) {
        return null;
    }

    public Mono<Void> deleteProduct(Long id) {
        return null;
    }
}
