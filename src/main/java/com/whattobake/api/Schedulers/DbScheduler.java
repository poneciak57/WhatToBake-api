package com.whattobake.api.Schedulers;

import com.whattobake.api.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAsync
@Component
@RequiredArgsConstructor
public class DbScheduler {

    private final ProductRepository productRepository;
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void keepDbAlive() {
        log.info("Daily db Scheduler proc :)");
        productRepository.count().subscribe();
    }
}
