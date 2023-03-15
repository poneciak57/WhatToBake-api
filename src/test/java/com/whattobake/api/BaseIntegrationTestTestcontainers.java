package com.whattobake.api;


import com.whattobake.api.Configuration.Neo4jConfiguration;
import com.whattobake.api.Mapers.ProductMapper;
import com.whattobake.api.Mapers.RecipeMapper;
import com.whattobake.api.Repository.Implementations.InitRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.Neo4jLabsPlugin;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Testcontainers
@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@Import({
        InitRepositoryImpl.class,
        ProductMapper.class,
        RecipeMapper.class,
        Neo4jConfiguration.class
})
public abstract class BaseIntegrationTestTestcontainers {

    @Container
    static final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:5.3")
            .withAdminPassword("testAdminPassword")
                .withLabsPlugins(Neo4jLabsPlugin.APOC)
                .withStartupTimeout(Duration.ofMinutes(5));

    @AfterAll
    public static void closeDB() {
        neo4jContainer.close();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        neo4jContainer.start();
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
        registry.add("database.init", () -> false);
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }
}
