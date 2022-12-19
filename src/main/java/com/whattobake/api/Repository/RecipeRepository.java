package com.whattobake.api.Repository;

import com.whattobake.api.Model.Recipe;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends ReactiveNeo4jRepository<Recipe,Long> {
}
