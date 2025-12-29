package com.sportmatch.identityservice.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Oauth2Info {
    private AuthProvider authProvider;
    private String providerId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}