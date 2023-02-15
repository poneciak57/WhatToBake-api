package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Dto.FilterDto.ProductFilters;
import com.whattobake.api.Enum.ProductOrder;
import com.whattobake.api.Mapers.ProductMapper;
import com.whattobake.api.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl {
    private final ProductMapper productMapper;

    @SuppressWarnings("unused")
    Flux<Product> findAll(ProductFilters productFilters){
        String q = """
            MATCH (p:PRODUCT)-[hc:HAS_CATEGORY]->(c:CATEGORY)
            RETURN"""+ ProductMapper.RETURN;
        if(!productFilters.getProductOrder().isEmpty()){
            q +=" ORDER BY " + productFilters.getProductOrder().stream()
                    .map(ProductOrder::getValue)
                    .collect(Collectors.joining(","));
        }
        return productMapper.resultAsFlux(productMapper.getMapperQueryNoAddon(q), Map.of());
    }

    @SuppressWarnings("unused")
    Mono<Product> create(Map<String,Object> product){
        String q = """
            CREATE (product:PRODUCT{name:$name})
            WITH product
            CALL{
                WITH product
                MATCH (c:CATEGORY) WHERE ID(c) = $category
                MERGE (product)-[:HAS_CATEGORY]->(c)
            }
            RETURN""";
        return productMapper.resultAsMono(productMapper.getMapperQuery(q),product);
    }
    @SuppressWarnings("unused")
    Mono<Product> update(Map<String,Object> product){
        String q = """
            MATCH (product:PRODUCT) WHERE ID(product) = $id
            SET product.name = $name
            WITH product
            CALL{ WITH product MATCH (product)-[r:HAS_CATEGORY]->(:CATEGORY) DELETE r }
            CALL{
                WITH product
                MATCH (c:CATEGORY) WHERE ID(c) = $category
                MERGE (product)-[:HAS_CATEGORY]->(c)
            }
            RETURN""";
        return productMapper.resultAsMono(productMapper.getMapperQuery(q),product);
    }
}
