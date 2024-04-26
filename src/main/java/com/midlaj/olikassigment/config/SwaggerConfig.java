package com.midlaj.olikassigment.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * Swagger configurations
     * @return
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Book Library Management RESTAPI")
                        .description("This is a  Spring Boot application that provides RESTful endpoints to manage a book library. The application should support CRUD operations for books, authors, and book rentals.")
                        .version("v1.0")
                        .license(new License().name("Muhammed Midlaj").url("http://github/miidlaj.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("POSTMAN API Collection with Documentation")
                        .url("https://www.postman.com/martian-sunset-628462/workspace/olik-assigment/collection/15935546-44af0d6b-a545-46b5-9263-1f682fa763a6?action=share&creator=15935546"));
    }

}
