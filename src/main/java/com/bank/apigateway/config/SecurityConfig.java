package com.bank.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                .authorizeExchange(ex -> ex
                        .pathMatchers("/api/auth/**", "/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )

                // ✅ NEW STYLE (Spring Security 6.1+)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {
                            // config via bean below
                        })
                )

                .build();
    }

    // 🔐 JWT DECODER (REQUIRED in new version)
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {

        // 👉 Example: Keycloak / Auth0 / custom issuer
        String jwkSetUri = "http://localhost:8089/realms/bank/protocol/openid-connect/certs";

        return NimbusReactiveJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .build();
    }
}