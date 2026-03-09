package com.petwise.infrastructure.configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Servlet filter that populates the SLF4J {@link MDC} with a unique {@code requestId}, the HTTP
 * {@code method}, and the request {@code uri} for every incoming HTTP request, enabling correlated
 * log tracing across all layers.
 *
 * <p>If the client sends an {@code X-Request-ID} header, its value is reused; otherwise a new UUID
 * is generated. The resolved request ID is also set on the HTTP response as the {@code
 * X-Request-ID} header so clients can correlate responses with server-side logs.
 *
 * <p>The MDC is always cleared in a {@code finally} block to prevent leaking context to the next
 * request handled by the same thread.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestCorrelationFilter implements Filter {
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String METHOD_KEY = "method";
    private static final String URI_KEY = "uri";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final int SHORT_ID_LENGTH = 8;
    private static final Logger LOG = LoggerFactory.getLogger(RequestCorrelationFilter.class);

    public RequestCorrelationFilter() {}

    @Override
    public void doFilter(
            final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        try {
            final String requestId = resolveRequestId(request);
            MDC.put(REQUEST_ID_KEY, requestId);

            if (request instanceof HttpServletRequest httpRequest) {
                MDC.put(METHOD_KEY, httpRequest.getMethod());
                MDC.put(URI_KEY, httpRequest.getRequestURI());
            }

            if (response instanceof HttpServletResponse httpResponse) {
                httpResponse.setHeader(REQUEST_ID_HEADER, requestId);
            }

            if (LOG.isTraceEnabled()) {
                LOG.trace("Started request processing");
            }

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private static String resolveRequestId(final ServletRequest request) {
        String requestId = UUID.randomUUID().toString().substring(0, SHORT_ID_LENGTH);

        if (request instanceof HttpServletRequest httpRequest) {
            final String header = httpRequest.getHeader(REQUEST_ID_HEADER);
            if (header != null && !header.isBlank()) {
                requestId = header;
            }
        }

        return requestId;
    }
}
