package com.whattobake.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whattobake.api.Dto.SseDto.PbAction;
import com.whattobake.api.Model.User;
import com.whattobake.api.Service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

import java.util.Objects;

@Slf4j
@SpringBootApplication
@EnableReactiveNeo4jRepositories
@OpenAPIDefinition(info = @Info(title = "What2Bake", version = "0.2", description = "Documentation APIs v0."))
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner (
			UserService userService
	){
		return args -> {
			// PocketBase User events handling //
			ObjectMapper mapper = new ObjectMapper();
			var eventStream = userService.connectToRealtime();
			eventStream.subscribe(
					content -> {
						if(Objects.equals(content.event(), "PB_CONNECT")){
							log.info("Connecting to PB realtime");
							userService.subscribeToUsers(content.id())
									.doOnSuccess(a -> log.info("Successfully established connection with PB realtime"))
									.subscribe();
						}else{
							try {
								var action = mapper.readValue(content.data(), PbAction.class);
								User u = User.fromPBAction(action.getRecord());
								switch (action.getAction()){
									case create -> userService.create(u).subscribe();
									case update -> userService.update(u).subscribe();
									case delete -> userService.delete(u).subscribe();
								}
								log.info(User.fromPBAction(action.getRecord()).toString());
							} catch (JsonProcessingException e) {
								log.error("Error while handling an event from PocketBase");
							}
						}
					},
					error -> log.error("Error receiving SSE: ", error),
					() -> log.info("Completed!!!"));
		};
	}
}
