package com.konkuk.vecto.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
	info = @Info(title = "Vecto App",
		description = "Vecto app api명세",
		version = "v1"),
	servers = {
		@Server(url = "https://vec-to.net"),
		@Server(url = "http://localhost:5000")
	}
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/**"};

		return GroupedOpenApi.builder()
			.group("Vecto API v1")
			.pathsToMatch(paths)
			.build();
	}
}