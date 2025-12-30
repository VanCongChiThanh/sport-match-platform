package com.sportmatch.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OauthAccessTokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String refreshToken,
        Timestamp createdAt
) {
    public OauthAccessTokenResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }

    public OauthAccessTokenResponse(String accessToken, long expiresIn, String refreshToken, Timestamp createdAt) {
        this(accessToken, "Bearer", expiresIn, refreshToken, createdAt);
    }
}