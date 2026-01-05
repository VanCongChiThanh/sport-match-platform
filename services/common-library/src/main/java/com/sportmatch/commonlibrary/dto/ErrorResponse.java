package com.sportmatch.commonlibrary.dto;

public record ErrorResponse(
        String code,
        String message
) {}