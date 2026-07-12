package com.microbase.commonlibrary.security;

import com.microbase.commonlibrary.constants.MessageConstant;
import com.microbase.commonlibrary.exception.ForbiddenException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CurrentUserProvider {

    public CurrentUser getCurrentUser() {
        JwtAuthenticationToken authentication = getJwtAuthentication();
        Jwt jwt = authentication.getToken();
        return new CurrentUser(
                jwt.getSubject(),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                extractRoles(authentication)
        );
    }

    public UUID getCurrentUserId() {
        return UUID.fromString(getCurrentUser().id());
    }

    public String getAccessToken() {
        return getJwtAuthentication().getToken().getTokenValue();
    }

    public boolean hasRole(String role) {
        return getCurrentUser().hasRole(role);
    }

    private JwtAuthenticationToken getJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
        }
        return jwtAuthenticationToken;
    }

    private Set<String> extractRoles(JwtAuthenticationToken authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }
}