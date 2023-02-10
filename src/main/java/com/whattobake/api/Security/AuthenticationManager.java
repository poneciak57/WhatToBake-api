package com.whattobake.api.Security;

import com.whattobake.api.Model.User;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Value("${pocketbase.url}")
    private String pocketbaseURL;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        return getUser(authToken)
                .switchIfEmpty(Mono.empty())
                .map(u -> {
                    var at = new UsernamePasswordAuthenticationToken(u.getName(),authToken,
                            u.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList())
                    );
                    at.setDetails(u);
                    return at;
                });
    }

    private Mono<User> getUser(String token) {
//      INFO Auth token Should be as follows "uid|jwtToken";
        String auth;
        String uid;
        try {
            String[] tt = token.split("\\|");
            uid = tt[0];
            auth = tt[1];
        } catch (Exception e) {
            return Mono.empty();
        }
        WebClient client = WebClient.builder()
                .baseUrl(pocketbaseURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + auth)
                .build();
        return client.get()
                .uri("/api/collections/users/records/" + uid)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(User.class);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
