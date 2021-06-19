package dev.strongtino.soteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceLauncher {

	public static void main(String[] args) {
		// Backend web server
		SpringApplication.run(ServiceLauncher.class);

		// Frontend discord bot
		Soteria.INSTANCE.start();
	}
}
