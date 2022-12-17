package com.whattobake.api.Repository;

import com.whattobake.api.Model.Product;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveNeo4jRepository<Product,Long> {
}
