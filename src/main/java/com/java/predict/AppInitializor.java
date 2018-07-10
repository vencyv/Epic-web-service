package com.java.predict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author RuchitM
 * Initializor class to initialize application as spring boot app
 */
@SpringBootApplication
@PropertySource(value={"classpath:constant.properties"})
public class AppInitializor {
	
	public static void main(String[] args) {
		SpringApplication.run(AppInitializor.class, args);
	}
}
