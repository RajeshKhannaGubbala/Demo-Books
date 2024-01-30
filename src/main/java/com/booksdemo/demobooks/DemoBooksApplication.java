package com.booksdemo.demobooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DemoBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBooksApplication.class, args);
	}

}
