package com.whattobake.api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AppController {

    @GetMapping("test/ping")
    public Mono<String> pong(){
        return Mono.just("pong");
    }
}
