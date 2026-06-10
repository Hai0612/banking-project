package com.bank.apigateway.filter;

import com.bank.apigateway.security.JwtDecoder;
import io.jsonwebtoken.Claims;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class GlobalUserContextFilter implements GlobalFilter, Ordered {

    private final JwtDecoder jwtDecoder; // inject JWT decoder của bạn

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        String authHeader = exchange.getRequest()
                .getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {

                Claims claims = jwtDecoder.decode(token);

                String userId = claims.getSubject();

                ServerHttpRequest mutated =
                        exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .build();

                return chain.filter(
                        exchange.mutate()
                                .request(mutated)
                                .build()
                );

            } catch (Exception ignored) {
                // Auth service xử lý 401
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() { return -2; }
}
