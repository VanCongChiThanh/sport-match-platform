package com.sportmatch.profileservice.common.response;

public record ErrorResponse(
        String code,
        String message
) {}