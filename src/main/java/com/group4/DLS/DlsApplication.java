package com.group4.DLS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DlsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlsApplication.class, args);
	}
	
}
