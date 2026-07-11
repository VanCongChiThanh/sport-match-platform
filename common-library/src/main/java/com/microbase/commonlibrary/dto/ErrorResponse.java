package com.microbase.commonlibrary.dto;

public record ErrorResponse(
        String code,
        String message
) {}