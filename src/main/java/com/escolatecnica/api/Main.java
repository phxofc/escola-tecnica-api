package com.escolatecnica.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.escolatecnica.api")
public class Main {

	public static ZonedDateTime START_UP_TIME = ZonedDateTime.now();

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
