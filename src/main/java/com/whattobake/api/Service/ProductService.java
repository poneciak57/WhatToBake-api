package com.whattobake.api.Service;

import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ReactiveNeo4jClient client;
    private final ReactiveNeo4jTemplate template;
    private final ProductRepository productRepository;

    public Flux<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Flux<Product> getAllProductsTemplate(){
        String query = """
            MATCH (product:PRODUCT) -[r:HAS_CATEGORY]-> (category:CATEGORY) RETURN product,r,category
        """;
        return template.findAll(query,Product.class);
    }
}
