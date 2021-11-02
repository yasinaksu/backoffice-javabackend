package com.omniteam.backofisbackend;

import com.omniteam.backofisbackend.security.config.WebSecurityConfig;
import com.omniteam.backofisbackend.swagger.Swagger2Configuration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableBatchProcessing
@EnableSwagger2
@SpringBootApplication
@Import( { WebSecurityConfig.class, Swagger2Configuration.class } )

public class BackofisbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackofisbackendApplication.class, args);
	}


	//Cors
/*
	@Bean
	public WebMvcConfigurer corsConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
						.allowedOrigins("*")
						.allowedHeaders("*")
				;
			}
		};
	}*/

}
