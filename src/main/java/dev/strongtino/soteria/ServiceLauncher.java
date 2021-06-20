package dev.strongtino.soteria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceLauncher {

	public static void main(String[] args) {
		// Web server
		SpringApplication.run(ServiceLauncher.class);

		// Discord bot
		Soteria.INSTANCE.start();
	}
}
