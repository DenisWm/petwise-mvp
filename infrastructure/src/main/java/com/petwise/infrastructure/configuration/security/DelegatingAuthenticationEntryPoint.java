package com.petwise.infrastructure.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Custom {@link AuthenticationEntryPoint} that delegates to the Spring MVC
 * {@link HandlerExceptionResolver} chain, so that security-related 401 errors
 * are handled by {@link com.petwise.infrastructure.api.GlobalExceptionHandler}
 * just like any other application exception.
 *
 * <p>This keeps all error-response formatting in a single place and avoids
 * duplicating JSON serialisation logic inside the security configuration.
 */
@Component
public class DelegatingAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    /**
     * Creates the entry point.
     *
     * @param resolver the Spring MVC exception resolver (the "handlerExceptionResolver" bean)
     */
    public DelegatingAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) {

        resolver.resolveException(request, response, null, authException);
    }
}

