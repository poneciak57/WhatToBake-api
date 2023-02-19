package com.whattobake.api.Service;

import com.whattobake.api.Model.User;
import com.whattobake.api.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${pocketbase.url}")
    private String pbURL;

    @Value("${pocketbase.admin.username}")
    private String pbAdminUsername;
    @Value("${pocketbase.admin.password}")
    private String pbAdminPassword;

    private final UserRepository userRepository;

    public Flux<ServerSentEvent<String>> connectToRealtime(){
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {};
        return WebClient.create(pbURL).get()
                .uri("/api/realtime")
                .retrieve()
                .bodyToFlux(type)
                .repeat();
    }

    public Mono<String> subscribeToUsers(String clientId){
        String body = "{\"clientId\":\""+clientId+"\",\"subscriptions\": [\"users\"]\n}";
        return this.getAdminAuthToken().flatMap(token ->
                WebClient.create(pbURL).post()
                .uri("/api/realtime")
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {}));
    }

    public Mono<User> create(User user){
        return userRepository.save(user);
    }

    public Mono<User> update(User user){
        return userRepository.save(user);
    }

    public Mono<Void> delete(User user){
        return userRepository.delete(user);
    }

    private Mono<String> getAdminAuthToken(){
        String body = "{\"identity\":\""+pbAdminUsername+"\",\"password\":\""+pbAdminPassword+"\"}";
        return WebClient.create(pbURL).post()
                .uri("/api/admins/auth-with-password")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(Map.class)
                .map(m -> m.get("token").toString());


    }
}
