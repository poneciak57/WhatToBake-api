package com.whattobake.api.Repository;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Model.Product;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public interface ProductRepository extends ReactiveNeo4jRepository<Product,Long> {

    Flux<Product> findAll(ProductFilters productFilters);

    Mono<Product> update(Map<String,Object> product);

    @Deprecated
    Mono<Product> create(Map<String,Object> product);
}
