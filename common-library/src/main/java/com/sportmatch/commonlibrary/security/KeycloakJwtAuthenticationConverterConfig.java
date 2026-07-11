package com.sportmatch.commonlibrary.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
public class KeycloakJwtAuthenticationConverterConfig {

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> Stream.concat(
                        extractRealmRoles(jwt).stream(),
                        extractClientRoles(jwt).stream()
                )
                .distinct()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    private static Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
        return realmAccess == null ? Collections.emptyList() : realmAccess.getOrDefault("roles", Collections.emptyList());
    }

    private static Collection<String> extractClientRoles(Jwt jwt) {
        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        return resourceAccess.values().stream()
                .flatMap(client -> client.getOrDefault("roles", Collections.emptyList()).stream())
                .toList();
    }
}
