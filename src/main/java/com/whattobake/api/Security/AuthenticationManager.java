package com.whattobake.api.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@Component
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        log.info("AuthManager auth proc");
        return getUser(authToken)
                .switchIfEmpty(Mono.empty())
                .map(u -> {
                    var at = new UsernamePasswordAuthenticationToken(u.getName(),authToken,
                            u.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                    at.setDetails(u);
                    return at;
                });
    }

    private Mono<PbUser> getUser(String token){
        WebClient client = WebClient.builder()
                .baseUrl("http://132.226.204.66:82")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .build();
//        TODO change id in link to current user
        return client.get()
                .uri("/api/collections/users/records/9biezmuz92cxl5q")
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(PbUser.class);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
