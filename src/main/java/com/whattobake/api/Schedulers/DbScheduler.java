package com.whattobake.api.Schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAsync
@Component
@RequiredArgsConstructor
public class DbScheduler {

    private final ReactiveNeo4jClient client;
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void keepDbAlive() {
        log.info("Daily db Scheduler proc :)");
        String q = """
            MATCH (n) RETURN COUNT(n)
        """;
        client.query(q).fetch().first().subscribe();
    }

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteUnusedUsers(){
        log.info("Daily db clean :)");
        String q = """
            MATCH (user:USER)
            CALL{ WITH user MATCH (user)-[r]-() RETURN COUNT(r) as rels }
            WITH user,rels
            MATCH (user) WHERE rels = 0 DELETE user
        """;
        client.query(q).fetch().first().subscribe();
    }
}
