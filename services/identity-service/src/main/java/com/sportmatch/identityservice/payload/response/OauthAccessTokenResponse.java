package com.sportmatch.identityservice.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OauthAccessTokenResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private long expiresIn;
  private String refreshToken;
  private Timestamp createdAt;
}