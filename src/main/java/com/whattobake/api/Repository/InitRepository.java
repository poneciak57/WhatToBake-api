package com.whattobake.api.Repository;

import org.neo4j.driver.summary.ResultSummary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InitRepository {

    Mono<ResultSummary> clearTestDatabase();
    Mono<ResultSummary> initTestDatabase();
}
