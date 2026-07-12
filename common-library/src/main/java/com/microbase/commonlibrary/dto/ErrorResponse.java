package com.microbase.commonlibrary.dto;

import com.microbase.commonlibrary.observability.CorrelationIdHolder;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        Integer status,
        String path,
        String requestId,
        Instant timestamp,
        List<ErrorDetail> details
) {
    public ErrorResponse(String code, String message) {
        this(code, message, null, null, CorrelationIdHolder.get(), Instant.now(), List.of());
    }

    public static ErrorResponse of(String code, String message, int status, String path) {
        return new ErrorResponse(code, message, status, path, CorrelationIdHolder.get(), Instant.now(), List.of());
    }

    public static ErrorResponse of(String code, String message, int status, String path, List<ErrorDetail> details) {
        return new ErrorResponse(code, message, status, path, CorrelationIdHolder.get(), Instant.now(), details);
    }
}