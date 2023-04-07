package com.whattobake.api.Controller;

import com.whattobake.api.Model.User;
import com.whattobake.api.Security.SecurityHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Tag(name ="7. Tests")
@RestController
@RequestMapping("/api")
public class AppController {

    @GetMapping("test/ping")
    public Mono<String> pong() {
        return Mono.just("pong");
    }

    @GetMapping("test/user")
    public Mono<User> user(Mono<Principal> principal) {
        return principal.map(SecurityHelper::UserFromPrincipal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("test/admin")
    public Mono<Principal> admin(Mono<Principal> principal) {
        return principal;
    }
}
