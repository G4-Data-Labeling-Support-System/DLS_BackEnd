package com.group4.DLS;

import com.group4.DLS.domain.entity.SeaweedFSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class DlsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlsApplication.class, args);
	}
	
}
