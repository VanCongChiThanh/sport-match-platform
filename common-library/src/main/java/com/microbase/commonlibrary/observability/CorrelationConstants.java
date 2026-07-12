package com.microbase.commonlibrary.observability;

public final class CorrelationConstants {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String MDC_CORRELATION_ID = "correlationId";

    private CorrelationConstants() {
    }
}