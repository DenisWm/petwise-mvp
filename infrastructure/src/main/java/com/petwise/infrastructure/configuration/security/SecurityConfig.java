package com.petwise.infrastructure.configuration.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.petwise.infrastructure.configuration.DelegatingAccessDeniedHandler;
import com.petwise.infrastructure.configuration.DelegatingAuthenticationEntryPoint;
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
 * Configures the application as an <strong>OAuth 2.0 Resource Server</strong>
 * that validates JWTs issued by Keycloak.
 *
 * <ul>
 *   <li>Stateless session — no JSESSIONID cookie.</li>
 *   <li>CSRF disabled (token-based auth, no browser cookies).</li>
 *   <li>Roles extracted from the {@code realm_access.roles} claim.</li>
 *   <li>Actuator health + OpenAPI endpoints remain public.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // enables @PreAuthorize / @Secured on controllers
@Profile("!test-integration")
public class SecurityConfig {

    // ── Public endpoint patterns ────────────────────────────────────────
    private static final String[] ACTUATOR_PATTERNS = {
        "/actuator/health/**",
        "/actuator/info"
    };

    private static final String[] OPENAPI_PATTERNS = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    private static final String CORS_PREFLIGHT_PATTERN = "/**";

    // ── Role-protected endpoint patterns ────────────────────────────────
    private static final String DAILY_AGENDA_PATTERN = "/appointments/agenda";
    private static final String APPOINTMENTS_PATTERN = "/appointments/**";
    private static final String TUTORS_PATTERN = "/tutors/**";
    private static final String PETS_PATTERN = "/pets/**";

    private final DelegatingAuthenticationEntryPoint authenticationEntryPoint;
    private final DelegatingAccessDeniedHandler accessDeniedHandler;

    /** Creates the security configuration.
     *
     * @param authenticationEntryPoint delegates 401 errors to GlobalExceptionHandler
     * @param accessDeniedHandler      delegates 403 errors to GlobalExceptionHandler
     */
    public SecurityConfig(
            DelegatingAuthenticationEntryPoint authenticationEntryPoint,
            DelegatingAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Main security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless — no server-side session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Disable CSRF — we rely on Bearer tokens, not cookies
            .csrf(AbstractHttpConfigurer::disable)

            // Authorisation rules
            .authorizeHttpRequests(auth -> auth
                // Public health / readiness probes
                .requestMatchers(ACTUATOR_PATTERNS).permitAll()
                // OpenAPI / Swagger UI
                .requestMatchers(OPENAPI_PATTERNS).permitAll()
                // Allow CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, CORS_PREFLIGHT_PATTERN).permitAll()
                // Role-protected endpoints — daily agenda
                .requestMatchers(HttpMethod.GET, DAILY_AGENDA_PATTERN)
                    .hasAnyRole("ADMIN", "ATTENDANT")
                // Role-protected endpoints — appointments
                .requestMatchers(APPOINTMENTS_PATTERN)
                    .hasAnyRole("ADMIN", "ATTENDANT")
                // Role-protected endpoints — tutors
                .requestMatchers(TUTORS_PATTERN)
                    .hasAnyRole("ADMIN", "ATTENDANT")
                // Role-protected endpoints — pets
                .requestMatchers(PETS_PATTERN)
                    .hasAnyRole("ADMIN", "ATTENDANT")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // Global error handling — covers EVERY 401/403 from any filter
            // (authorization rules, @PreAuthorize, missing/bad token, etc.)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )

            // Validate JWTs issued by Keycloak
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * Converts Keycloak's {@code realm_access.roles} array into Spring Security
     * {@link GrantedAuthority} instances so that {@code @PreAuthorize("hasRole('ADMIN')")}
     * and {@code hasAuthority('ROLE_ADMIN')} work transparently.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }


    /**
     * Extracts roles from the standard Keycloak JWT structure:
     * <pre>
     * {
     *   "realm_access": {
     *     "roles": ["ROLE_ADMIN", "ROLE_ATTENDANT", ...]
     *   }
     * }
     * </pre>
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }

            Object rolesObj = realmAccess.get("roles");
            if (!(rolesObj instanceof List<?>)) {
                return Collections.emptyList();
            }

            return ((List<String>) rolesObj).stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
        }
    }
}



