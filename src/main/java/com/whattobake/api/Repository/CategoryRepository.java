package com.whattobake.api.Repository;

import com.whattobake.api.Model.Category;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ReactiveNeo4jRepository<Category,Long> {
}
