package com.sportmatch.identityservice.service;


import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.payload.response.Oauth2Info;

public interface Oauth2LoginStrategy {
    Oauth2Info process(String accessToken);
    AuthProvider getProvider();
}