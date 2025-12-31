package com.carmgmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Car Management & Fuel Tracking API")
                        .description("REST API for managing cars and tracking fuel consumption. " +
                                "This API allows you to create cars, add fuel entries, and retrieve fuel statistics.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Car Management API")
                                .email("support@carmgmt.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

