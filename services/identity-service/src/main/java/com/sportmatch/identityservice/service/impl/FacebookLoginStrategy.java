package com.sportmatch.identityservice.service.impl;


import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.dto.response.Oauth2Info;
import com.sportmatch.identityservice.service.Oauth2LoginStrategy;
import org.springframework.stereotype.Service;

@Service
public class FacebookLoginStrategy implements Oauth2LoginStrategy {

    @Override
    public Oauth2Info process(String accessToken) {
        // 1. Call Facebook API to get user info (email, id, name)
        // 2. Check UserProvider, if not exists, create new User and UserProvider
        // 3. Return User
        return null; // Replace with actual implementation
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.FACEBOOK; // Return the provider type
    }
}