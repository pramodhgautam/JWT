package com.nchl.JWT;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@PostConstruct
	public void init() {
		System.out.println("JVM Timezone: " + TimeZone.getDefault().getID());
		System.out.println("Current time: " + new Date());
	}

}
