package com.webpick.initializr.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI webpickOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Webpick Initializr API")
                        .description("REST API for bootstrapping and generating customized project configurations across multiple backend and frontend technologies.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Webpick Team")
                                .url("https://github.com/abderrahmanBouanani/Webpick-Initializr"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
