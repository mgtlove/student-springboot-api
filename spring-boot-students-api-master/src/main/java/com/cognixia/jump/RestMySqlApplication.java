package com.cognixia.jump;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.maxmind.geoip2.DatabaseReader;

@SpringBootApplication
public class RestMySqlApplication {


	//@Scope(value = "prototype")
	@Bean
	public DatabaseReader dbReader() throws IOException {

		File database = new File("GeoLite2-City_20210928/GeoLite2-City.mmdb");
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
		return dbReader;
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(RestMySqlApplication.class, args);
	}

}
