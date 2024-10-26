package com.zeotap.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;

@EnableScheduling
@SpringBootApplication
public class WeatherMonitorAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMonitorAppApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(Environment env) {
	    return args -> {
	        System.out.println("Available properties:");
	        for (String property : env.getActiveProfiles()) {
	            System.out.println(property + ": " + env.getProperty(property));
	        }
	    };
	}


}
