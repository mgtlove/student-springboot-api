package com.cognixia.jump;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestMySqlApplication {
	
	@Value("${JDBC_CONNECTION_STRING}")
	private static final String JDBC_CONNECTION_STRING = System.getenv("JDBC_CONNECTION_STRING");

	public static void main(String[] args) {
		
		System.out.println(JDBC_CONNECTION_STRING);
		
		SpringApplication.run(RestMySqlApplication.class, args);
	}

}
