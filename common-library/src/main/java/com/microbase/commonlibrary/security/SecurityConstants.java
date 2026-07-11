package com.microbase.commonlibrary.security;

public final class SecurityConstants {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_SERVICE = "SERVICE";

    public static final String[] DEFAULT_PUBLIC_ENDPOINTS = {
            "/actuator/health/**",
            "/error",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private SecurityConstants() {
    }
}