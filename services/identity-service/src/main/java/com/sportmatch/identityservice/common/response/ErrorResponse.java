package com.sportmatch.identityservice.common.response;

public record ErrorResponse(
        String code,
        String message
) {}