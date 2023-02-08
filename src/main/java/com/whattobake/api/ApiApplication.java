package com.whattobake.api;

import com.whattobake.api.Service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

import java.time.LocalTime;
import java.util.Objects;

@Slf4j
@SpringBootApplication
@EnableReactiveNeo4jRepositories
@OpenAPIDefinition(info = @Info(title = "What2Bake", version = "0.1", description = "Documentation APIs v0."))
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner (
			UserService userService
	){
		return args -> {

			/*
			* TODO listening for users SSE
			* DONE 1. Connect to get endpoint and establish connection
			* DONE 2. get connectionId with PB_CONNECT event
			* 3. send post request to subscribe for users collection
			* 4. handle user events
			* */

//			WebClient client = WebClient.builder()
//					.baseUrl(pocketbaseURL)
//					.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzcwOTE5NTAsImlkIjoiZ25tZ2Z0MDNkbnJvOTYyIiwidHlwZSI6ImFkbWluIn0.LNLB59TRtqXv021pGKoZYJa2n7q4Dec7NKlygmJ4m5U")
//					.build();
			var eventStream = userService.connectToRealtime();
			eventStream.subscribe(
					content -> {
						if(Objects.equals(content.event(), "PB_CONNECT")){
							userService.subscribeToUsers(content.id());
						}else{
//							get pbAction object from event data and process an action
						}
						log.info("Time: {} - event: name[{}], id [{}], content[{}] ",
								LocalTime.now(), content.event(), content.id(), content.data());
					},
					error -> log.info("Error receiving SSE: ", error),
					() -> log.info("Completed!!!"));
		};
	}


}
