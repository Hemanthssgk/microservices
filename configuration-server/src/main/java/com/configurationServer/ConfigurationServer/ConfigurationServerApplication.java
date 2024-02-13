package com.configurationServer.ConfigurationServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
// this will enable configuration server related configurations
@EnableConfigServer
public class ConfigurationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationServerApplication.class, args);
	}

}
