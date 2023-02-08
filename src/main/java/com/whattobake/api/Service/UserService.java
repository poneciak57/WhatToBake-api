package com.whattobake.api.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class UserService {
    @Value("${pocketbase.url}")
    private String pocketbaseURL;



    public Flux<ServerSentEvent<String>> connectToRealtime(){
        WebClient client = WebClient.create(pocketbaseURL);
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};
        return client.get()
                .uri("/api/realtime")
                .retrieve()
                .bodyToFlux(type);
    }

    public void subscribeToUsers(String clientId){
//        TODO implement subscribe to user method
//        get the admin credentials to subscribe
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
