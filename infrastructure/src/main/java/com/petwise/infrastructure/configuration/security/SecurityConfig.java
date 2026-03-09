package com.petwise.infrastructure.configuration.security;

import com.petwise.infrastructure.configuration.DelegatingAccessDeniedHandler;
import com.petwise.infrastructure.configuration.DelegatingAuthenticationEntryPoint;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth 2.0 Resource Server configuration — validates Keycloak-issued JWTs, enforces role-based
 * access, and runs fully stateless (no JSESSIONID, no CSRF).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test-integration")
public class SecurityConfig {
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_ATTENDANT = "ATTENDANT";
    private static final String[] ACTUATOR_PATTERNS = {"/actuator/health/**", "/actuator/info"};
    private static final String[] OPENAPI_PATTERNS = {
        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };
    private static final String CORS_PATTERN = "/**";
    private static final String AGENDA_PATTERN = "/appointments/agenda";
    private static final String APPOINTMENTS = "/appointments/**";
    private static final String TUTORS = "/tutors/**";
    private static final String PETS = "/pets/**";
    private final DelegatingAuthenticationEntryPoint authEntryPoint;
    private final DelegatingAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            final DelegatingAuthenticationEntryPoint authEntryPoint,
            final DelegatingAccessDeniedHandler accessDeniedHandler) {
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(ACTUATOR_PATTERNS)
                                        .permitAll()
                                        .requestMatchers(OPENAPI_PATTERNS)
                                        .permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, CORS_PATTERN)
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, AGENDA_PATTERN)
                                        .hasAnyRole(ROLE_ADMIN, ROLE_ATTENDANT)
                                        .requestMatchers(APPOINTMENTS)
                                        .hasAnyRole(ROLE_ADMIN, ROLE_ATTENDANT)
                                        .requestMatchers(TUTORS)
                                        .hasAnyRole(ROLE_ADMIN, ROLE_ATTENDANT)
                                        .requestMatchers(PETS)
                                        .hasAnyRole(ROLE_ADMIN, ROLE_ATTENDANT)
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(
                        ex ->
                                ex.authenticationEntryPoint(authEntryPoint)
                                        .accessDeniedHandler(accessDeniedHandler))
                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt ->
                                                jwt.jwtAuthenticationConverter(
                                                        jwtAuthenticationConverter())));
        return http.build();
    }

    /**
     * Maps Keycloak's {@code realm_access.roles} to Spring Security {@link GrantedAuthority}s so
     * that {@code hasRole("ADMIN")} works transparently.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }

    /**
     * Extracts roles from {@code realm_access.roles} in the Keycloak JWT, prefixing each with
     * {@code ROLE_} if not already present.
     */
    @SuppressWarnings("PMD.OnlyOneReturn")
    static class KeycloakRealmRoleConverter
            implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }
            final Object rolesObj = realmAccess.get("roles");
            if (!(rolesObj instanceof List<?>)) {
                return Collections.emptyList();
            }
            return ((List<String>) rolesObj)
                    .stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toUnmodifiableList());
        }
    }
}
