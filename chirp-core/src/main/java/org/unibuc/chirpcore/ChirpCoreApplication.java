package org.unibuc.chirpcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class ChirpCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChirpCoreApplication.class, args);
	}

}
