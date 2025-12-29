package com.sportmatch.identityservice.repository;

import com.sportmatch.identityservice.entity.OauthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OauthAccessTokenRepository extends JpaRepository<OauthAccessToken, UUID> {
    Optional<OauthAccessToken> findByRefreshToken(UUID refreshToken);

    @Modifying
    @Query(
            nativeQuery = true,
            value =
                    "update oauth_access_tokens set revoked_at = now() where user_id =:user_id and revoked_at isnull")
    void revokeAll(@Param("user_id") UUID userId);

    @Modifying
    @Query(
            nativeQuery = true,
            value =
                    "update oauth_access_tokens set revoked_at = now() where user_id =:user_id and revoked_at isnull and id <> :token_id")
    void revokeAll(@Param("user_id") UUID userId, @Param("token_id") UUID tokenId);

}