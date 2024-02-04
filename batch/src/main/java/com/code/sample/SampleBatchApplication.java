package com.code.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SampleBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleBatchApplication.class, args);
	}

}
