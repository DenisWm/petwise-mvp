package com.petwise.infrastructure.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Custom {@link AccessDeniedHandler} that delegates to the Spring MVC
 * {@link HandlerExceptionResolver} chain, so that security-related 403 errors
 * are handled by {@link com.petwise.infrastructure.api.GlobalExceptionHandler}
 * just like any other application exception.
 *
 * <p>This keeps all error-response formatting in a single place and avoids
 * duplicating JSON serialisation logic inside the security configuration.
 */
@Component
public class DelegatingAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    /**
     * Creates the handler.
     *
     * @param resolver the Spring MVC exception resolver (the "handlerExceptionResolver" bean)
     */
    public DelegatingAccessDeniedHandler(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) {

        resolver.resolveException(request, response, null, accessDeniedException);
    }
}

