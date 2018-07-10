package com.java.predict.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.java.predict.service.EpicDataService;
import com.java.predict.service.EpicDataServiceImpl;
import com.java.predict.utility.ErrorUtil;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author RuchitM
 * Configuration class to configure Swagger and  EpicDataService
 */
@Configuration
@EnableSwagger2
public class AppConfig {
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
	@Bean
	public EpicDataService epicDataServiceImpl()
	{
		return new EpicDataServiceImpl();
	}
	@Bean
	public ErrorUtil errorUtil()
	{
		return new ErrorUtil();
	}
}
