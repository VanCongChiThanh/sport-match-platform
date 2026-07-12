package com.microbase.commonlibrary.observability;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String correlationId = resolveCorrelationId(request);
        CorrelationIdHolder.set(correlationId);
        MDC.put(CorrelationConstants.MDC_CORRELATION_ID, correlationId);
        response.setHeader(CorrelationConstants.CORRELATION_ID_HEADER, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CorrelationConstants.MDC_CORRELATION_ID);
            CorrelationIdHolder.clear();
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CorrelationConstants.CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = request.getHeader(CorrelationConstants.REQUEST_ID_HEADER);
        }
        return correlationId == null || correlationId.isBlank() ? UUID.randomUUID().toString() : correlationId;
    }
}