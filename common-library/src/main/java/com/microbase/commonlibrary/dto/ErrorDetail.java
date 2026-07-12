package com.microbase.commonlibrary.dto;

public record ErrorDetail(
        String field,
        String code,
        String message
) {
}