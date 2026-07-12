package com.microbase.commonlibrary.security;

import java.util.Set;

public record CurrentUser(
        String id,
        String username,
        String email,
        Set<String> roles
) {
    public boolean hasRole(String role) {
        return roles.contains(role) || roles.contains("ROLE_" + role);
    }
}