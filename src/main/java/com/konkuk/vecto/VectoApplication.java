package com.konkuk.vecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.parameters.P;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class VectoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VectoApplication.class, args);
	}



}
