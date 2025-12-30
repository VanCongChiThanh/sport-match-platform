package com.sportmatch.identityservice.dto.response;

public record ErrorResponse(
        String code,
        String message
) {}