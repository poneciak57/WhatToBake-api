package com.whattobake.api.Controller;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class AppController {

    @GetMapping("test/ping")
    public Mono<String> pong(){
        return Mono.just("pong");
    }


    @GetMapping("test/user")
    public Mono<Principal> user(Mono<Principal> principal){
        return principal;
    }

    @PostAuthorize("hasRole('ADMIN')")
    @GetMapping("test/admin")
    public Mono<Principal> admin(Mono<Principal> principal){
        return principal;
    }
}
