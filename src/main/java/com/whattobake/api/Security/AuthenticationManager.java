package com.whattobake.api.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whattobake.api.Dto.SecurityDto.PbUser;
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

import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Value("${pocketbase.url}")
    private String pocketbaseURL;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String uid;
        try {
            uid = (String) (new ObjectMapper()).readValue(new String(Base64.getUrlDecoder().decode(authToken.split("\\.")[1])), Map.class).get("id");
        } catch (Exception e) { return Mono.empty(); }
        return getUser(uid,authToken)
                .switchIfEmpty(Mono.empty())
                .map(User::fromPbUser)
                .map(u -> {
                    var at = new UsernamePasswordAuthenticationToken(u.getName(),authToken,
                            u.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList())
                    );
                    at.setDetails(u);
                    return at;
                });
    }

    private Mono<PbUser> getUser(String uid,String token) {
        WebClient client = WebClient.builder()
                .baseUrl(pocketbaseURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        return client.get()
                .uri("/api/collections/users/records/" + uid)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(PbUser.class);
                    }
                    return Mono.empty();
                });
    }
}
