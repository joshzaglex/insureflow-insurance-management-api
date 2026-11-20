package com.joshua.insureflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InsureflowApplication {

	public static void main(String[] args) {

		SpringApplication.run(InsureflowApplication.class, args);
	}

}
