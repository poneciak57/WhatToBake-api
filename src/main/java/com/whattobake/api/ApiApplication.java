package com.whattobake.api;

import com.whattobake.api.Repository.InitRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

@Slf4j
@SpringBootApplication
@EnableReactiveNeo4jRepositories
@OpenAPIDefinition(info = @Info(title = "What2Bake", version = "0.2", description = "Documentation APIs v0."))
public class ApiApplication {

	@Value("${database.init}")
	private boolean dbInit;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner (
			InitRepository initRepository
	){
		return args -> {
			if(dbInit){
				initRepository.clearTestDatabase()
						.then(initRepository.initTestDatabase())
						.subscribe();
			}
		};
	}

}
