package eu.euromov.activmotiv;

import org.springframework.boot.SpringApplication;

import eu.euromov.activmotiv.config.ActivMotivConfig;

public class ActivMotivServer {
	
	public static void main(String[] args) {
		SpringApplication.run(ActivMotivConfig.class, args);
	}
}
