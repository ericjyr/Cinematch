package com.cm.cinematchapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The `CinematchAppApplication` class is the entry point for the Cinematch application.
 * It configures and launches the Spring Boot application.
 *
 * @author Eric Rebadona
 */
@SpringBootApplication
@EnableAsync
public class CinematchAppApplication {

	/**
	 * The main method that starts the Cinematch application.
	 *
	 * @param args The command-line arguments passed to the application (unused in this application).
	 */
	public static void main(String[] args) {
		SpringApplication.run(CinematchAppApplication.class, args);
	}

}
