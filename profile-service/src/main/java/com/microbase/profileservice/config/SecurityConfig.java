package com.microbase.profileservice.config;

import com.microbase.commonlibrary.security.ResourceServerSecurityConfigurer;
import com.microbase.commonlibrary.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ResourceServerSecurityConfigurer resourceServerSecurityConfigurer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return resourceServerSecurityConfigurer.applyDefaults(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConstants.DEFAULT_PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}