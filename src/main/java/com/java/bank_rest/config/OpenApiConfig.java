package com.java.bank_rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bankRestOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank REST API")
                        .description("Interactive documentation for Bank REST Application")
                        .version("v1.0.0")
                );
    }
}
