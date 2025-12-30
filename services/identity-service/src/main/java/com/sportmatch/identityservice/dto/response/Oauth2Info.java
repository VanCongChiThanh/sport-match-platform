package com.sportmatch.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record Oauth2Info(
        AuthProvider authProvider,
        String providerId,
        String email,
        String firstName,
        String lastName,
        String avatarUrl
) {}