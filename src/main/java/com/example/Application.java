package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Here is the starting point of the application.
 * 
 * It uses Spring Boot, Spring MVC, Spring Data, Maven, an embedded MongoDB, and Logback.
 * 
 * External configuration is done via the 'application.properties'.
 * 
 * It also activates scheduling that is used by class EuroDollarExchangeRateChecker to poll exchange rates from a public
 * Rest service.
 * 
 * This application uses Spring Data with an embedded MongoDB - but with Spring Data the database can be easily switched
 * to a different one.
 * 
 * Logging is done via Logback.
 * 
 * @author Kristian Lange (2017)
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
