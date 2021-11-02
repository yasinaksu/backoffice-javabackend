package com.omniteam.backofisbackend.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
@Order(2)
public class Swagger2Configuration {

	@Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiEndPointsInfo())
        		.securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
        		.select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.omniteam.backofisbackend.controller"))
                
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiEndPointsInfo() {

        return new ApiInfoBuilder().title("BackOffice Application")
                .description("BackOffice Application REST API Layer")
                .contact(new Contact("Omini Dev Team", "etiya.com", "info@etiya.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0")
                .build();
    }
    
    private ApiKey apiKey() { 
    	
        return new ApiKey("JWT", "Authorization", "header"); 
    }
    
    private SecurityContext securityContext() { 
    	
        return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
    } 

    private List<SecurityReference> defaultAuth() { 
    	
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
        authorizationScopes[0] = authorizationScope; 
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
    }
}
