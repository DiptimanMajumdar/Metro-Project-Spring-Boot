package com.metro.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.metro")
public class MetroSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetroSystemApplication.class, args);
	}

}
