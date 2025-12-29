package com.sportmatch.identityservice.service;



import com.sportmatch.identityservice.entity.OauthAccessToken;
import com.sportmatch.identityservice.entity.enums.AuthProvider;

import java.util.UUID;

public interface OauthAccessTokenService {
  /**
   * Create token
   *
   * @param userId User's id
   * @param refreshToken refreshToken
   * @return OauthAccessToken
   */
  OauthAccessToken createToken(UUID userId, UUID refreshToken, AuthProvider provider);

  /**
   * Get OauthAccessToken by id
   *
   * @param id auth token's id
   * @return OauthAccessToken
   */
  OauthAccessToken getOauthAccessTokenById(UUID id);

  /**
   * Get OauthAccessToken by refreshToken
   *
   * @param id Auth token's id
   * @return OauthAccessToken
   */
  OauthAccessToken getOauthAccessTokenByRefreshToken(UUID id);

  /**
   * Revoke token
   *
   * @param oauthAccessToken {@link OauthAccessToken}
   */
  void revoke(OauthAccessToken oauthAccessToken);

  /**
   * Revoke all token
   *
   * @param oauthAccessToken {@link OauthAccessToken}
   * @param email Email
   */
  void revokeAll(OauthAccessToken oauthAccessToken, String email);
}