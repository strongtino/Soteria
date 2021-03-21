package dev.strongtino.soteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceLauncher {

	public static void main(String[] args) {
		// Starting Discord bot service
		new Soteria().start();

		// Starting Spring service
		SpringApplication.run(ServiceLauncher.class);
	}
}
