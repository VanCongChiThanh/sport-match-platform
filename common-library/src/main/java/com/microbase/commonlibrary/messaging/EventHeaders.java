package com.microbase.commonlibrary.messaging;

public final class EventHeaders {

    public static final String EVENT_ID = "X-Event-Id";
    public static final String EVENT_TYPE = "X-Event-Type";
    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String SOURCE = "X-Source";

    private EventHeaders() {
    }
}