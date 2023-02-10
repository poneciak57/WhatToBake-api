package com.whattobake.api.Controller;

import com.whattobake.api.Model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Tag(name ="6. Tests")
@RestController
public class AppController {

    @GetMapping("test/ping")
    public Mono<String> pong(){
        return Mono.just("pong");
    }


    @GetMapping("test/user")
    public Mono<User> user(Mono<Principal> principal){
        return principal.map(p -> (User)((UsernamePasswordAuthenticationToken)p).getDetails());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("test/admin")
    public Mono<Principal> admin(Mono<Principal> principal){
        return principal;
    }
}
