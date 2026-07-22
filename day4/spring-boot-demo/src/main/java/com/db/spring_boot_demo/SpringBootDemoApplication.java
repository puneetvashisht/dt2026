package com.db.spring_boot_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDemoApplication implements CommandLineRunner {

	@Autowired
	A a;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		a.execute();
	}
}
