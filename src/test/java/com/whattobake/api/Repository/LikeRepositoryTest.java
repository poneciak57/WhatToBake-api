package com.whattobake.api.Repository;

import com.whattobake.api.BaseIntegrationTestEmbeddedDB;
import com.whattobake.api.Repository.Implementations.LikeRepositoryImpl;
import com.whattobake.api.Repository.Implementations.RecipeRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.Neo4jClient;

@Slf4j
@Import({LikeRepositoryImpl.class, RecipeRepositoryImpl.class})
class LikeRepositoryTest extends BaseIntegrationTestEmbeddedDB {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private Neo4jClient client;

    @BeforeEach
    public void setUp() {
//        TODO delete all nodes and relations
    }
}