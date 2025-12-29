package com.sportmatch.identityservice.service.impl;

import com.sportmatch.identityservice.config.AppProperties;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.payload.response.Oauth2Info;
import com.sportmatch.identityservice.service.Oauth2LoginStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleLoginStrategy implements Oauth2LoginStrategy {
    private final AppProperties appProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Oauth2Info process(String code) {
        String clientId = appProperties.getOauth2().getProviders().get("google").getClientId();
        String clientSecret = appProperties.getOauth2().getProviders().get("google").getClientSecret();
        String redirectUri = appProperties.getOauth2().getProviders().get("google").getRedirectUri();
        // Exchange code for access_token
        String tokenUrl = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to exchange code for access token from Google");
        }

        String accessToken = (String) response.getBody().get("access_token");

        // Get user info from Google
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.setBearerAuth(accessToken);

        HttpEntity<?> userInfoRequest = new HttpEntity<>(authHeader);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                userInfoRequest,
                Map.class
        );

        if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfoResponse.getBody() == null) {
            throw new RuntimeException("Failed to get user info from Google");
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String googleId = (String) userInfo.get("sub");
        String firstName = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");
        String email = (String) userInfo.get("email");
        String avatarUrl = (String) userInfo.get("picture");
        return Oauth2Info.builder().providerId(googleId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .avatarUrl(avatarUrl)
                .build();
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
    }
}