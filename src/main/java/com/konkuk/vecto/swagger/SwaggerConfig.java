package com.konkuk.vecto.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.Elements.JWT;

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

	@Bean
	public OpenAPI getOpenApi() {
		Components components = new Components()
				.addSecuritySchemes(JWT, getJwtSecurityScheme());
		SecurityRequirement securityItem = new SecurityRequirement()
				.addList(JWT);

		return new OpenAPI()
				.components(components)
				.addSecurityItem(securityItem);
	}
	private SecurityScheme getJwtSecurityScheme() {
		return new SecurityScheme()
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER)
				.name("AUTHORIZATION");}
}