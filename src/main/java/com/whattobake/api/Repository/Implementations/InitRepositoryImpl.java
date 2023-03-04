package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Repository.InitRepository;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.summary.ResultSummary;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitRepositoryImpl implements InitRepository {

    private final ReactiveNeo4jClient client;

    @Override
    public Mono<ResultSummary> clearTestDatabase() {
        String clear = "MATCH (n) DETACH DELETE n;";
        return client.query(clear).run();
    }

    @Override
    public Mono<ResultSummary> initTestDatabase() {
        String init;
        try {
            File file = ResourceUtils.getFile("classpath:init.cypher");
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            init = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return client.query(init).run();
    }
}
