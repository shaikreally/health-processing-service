package com.example.health_processing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthProcessingServiceApplication.class, args);
	}

}
