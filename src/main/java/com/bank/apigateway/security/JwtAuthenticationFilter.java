package com.bank.apigateway.security;


import com.core.common.SecurityConstants;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtValidator jwtValidator;

    public JwtAuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // public endpoints
        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        if (token == null || !jwtValidator.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtValidator.getClaims(token);

        String userId = claims.getSubject();
        String roles = String.valueOf(claims.get("roles"));

        ServerWebExchange mutated = exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header(SecurityConstants.HEADER_USER_ID, userId)
                        .header(SecurityConstants.HEADER_ROLES, roles)
                        .build())
                .build();

        return chain.filter(mutated);
    }

    private boolean isPublic(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/actuator")
                || path.startsWith("/public");
    }

    private String extractToken(ServerWebExchange exchange) {
        List<String> authHeaders = exchange.getRequest()
                .getHeaders()
                .getOrEmpty(SecurityConstants.AUTH_HEADER);

        if (authHeaders.isEmpty()) return null;

        String header = authHeaders.get(0);

        if (header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return header.substring(7);
        }

        return null;
    }

    @Override
    public int getOrder() {
        return -100; // run early in filter chain
    }
}