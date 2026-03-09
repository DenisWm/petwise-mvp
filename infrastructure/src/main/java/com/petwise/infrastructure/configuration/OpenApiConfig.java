package com.petwise.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata configuration.
 *
 * <p>Provides the global API title, version, description, and server URL that Springdoc embeds into
 * the generated specification. This replaces any manually maintained {@code openapi.yaml} header.
 *
 * <p>An <em>OAuth 2.0 Password</em> (Resource Owner Password Credentials) security scheme is
 * registered globally so that the Swagger UI displays an <strong>Authorize</strong> dialog with
 * username / password fields. Clicking <em>Authorize</em> fetches a JWT from Keycloak automatically
 * — no curl or copy-paste required.
 */
@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "keycloak-oauth";

    public OpenApiConfig() {}

    /**
     * Creates the global {@link OpenAPI} bean with PetWise metadata.
     *
     * @param keycloakIssuerUri the Keycloak issuer URI (e.g. {@code
     *     http://localhost:9080/realms/petwise})
     * @return the OpenAPI metadata
     */
    @Bean
    public OpenAPI petwiseOpenAPI(@Value("${keycloak.issuer-uri}") final String keycloakIssuerUri) {

        final String tokenUrl = keycloakIssuerUri + "/protocol/openid-connect/token";

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
                                        .description("Local development")))
                // --- Security: OAuth 2.0 Password flow (Keycloak) ---
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.OAUTH2)
                                                .description(
                                                        "Authenticate with Keycloak using username & password. "
                                                                + "Use client-id: petwise-swagger (no secret needed).")
                                                .flows(
                                                        new OAuthFlows()
                                                                .password(
                                                                        new OAuthFlow()
                                                                                .tokenUrl(
                                                                                        tokenUrl)))))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
