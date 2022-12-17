package com.whattobake.api;

import com.whattobake.api.Repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;

@SpringBootApplication
@EnableReactiveNeo4jRepositories
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	public CommandLineRunner commandLineRunner (
			ProductRepository productRepository
	){
		return args -> {

		};
	}
}
