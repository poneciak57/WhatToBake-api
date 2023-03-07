package com.whattobake.api;

import com.whattobake.api.Configuration.Neo4jConfiguration;
import com.whattobake.api.Mapers.ProductMapper;
import com.whattobake.api.Mapers.RecipeMapper;
import com.whattobake.api.Repository.Implementations.InitRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@Slf4j
@Import({
        InitRepositoryImpl.class,
        ProductMapper.class,
        RecipeMapper.class,
        Neo4jConfiguration.class
})
public abstract class BaseIntegrationTest {

    private static Neo4j embeddedDatabaseServer;

    @BeforeAll
    static void initializeNeo4j() {

        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()//disable http server
                .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
        registry.add("database.init",() -> false);
    }

    @AfterAll
    static void stopNeo4j() {
        embeddedDatabaseServer.close();
    }
}
