package com.petwise.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata configuration.
 *
 * <p>Provides the global API title, version, description, and server URL that Springdoc embeds into
 * the generated specification. This replaces any manually maintained {@code openapi.yaml} header.
 */
@Configuration
public class OpenApiConfig {

    /** Default constructor. */
    public OpenApiConfig() {}

    /**
     * Creates the global {@link OpenAPI} bean with PetWise metadata.
     *
     * @return the OpenAPI metadata
     */
    @Bean
    public OpenAPI petwiseOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("PetWise API")
                                .version("0.1.0")
                                .description(
                                        "PetWise MVP API for managing tutors, pets and appointments"
                                                + " (daycare/hotel scheduling)."))
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080/api")
                                        .description("Local development")));
    }
}
