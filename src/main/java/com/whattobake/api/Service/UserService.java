package com.whattobake.api.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Value("${pocketbase.url}")
    private String pbURL;

    @Value("${pocketbase.admin.token}")
    private String pbAdminAuthToken;


    public Flux<ServerSentEvent<String>> connectToRealtime(){
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};
        return WebClient.create(pbURL).get()
                .uri("/api/realtime")
                .retrieve()
                .bodyToFlux(type);
    }

    public Mono<String> subscribeToUsers(String clientId){
        String body = "{\"clientId\":\""+clientId+"\",\"subscriptions\": [\"users\"]\n}";
        return WebClient.create(pbURL).post()
                .uri("/api/realtime")
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+pbAdminAuthToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public void create(){
//        TODO implement create user event
    }

    public void update(){
//        TODO implement update user event
    }

    public void delete(){
//        TODO implement delete user event
    }
}
