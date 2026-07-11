package com.microbase.commonlibrary.security;

public final class SecurityExpressions {

    public static final String IS_ADMIN = "hasRole('ADMIN')";
    public static final String IS_SERVICE = "hasRole('SERVICE')";
    public static final String USER_OR_ADMIN = "hasAnyRole('USER', 'ADMIN')";
    public static final String SERVICE_OR_ADMIN = "hasAnyRole('SERVICE', 'ADMIN')";

    private SecurityExpressions() {
    }
}