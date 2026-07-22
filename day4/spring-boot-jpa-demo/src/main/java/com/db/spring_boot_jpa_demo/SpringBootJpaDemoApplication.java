package com.db.spring_boot_jpa_demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringBootJpaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaDemoApplication.class, args);
	}

}
