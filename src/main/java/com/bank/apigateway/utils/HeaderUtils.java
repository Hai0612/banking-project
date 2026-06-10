package com.bank.apigateway.utils;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

public class HeaderUtils {

    public static Optional<String> getHeader(ServerHttpRequest request, String key) {
        return Optional.ofNullable(request.getHeaders().getFirst(key));
    }

    public static String requireHeader(ServerHttpRequest request, String key) {
        String value = request.getHeaders().getFirst(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing required header: " + key);
        }
        return value;
    }

    public static ServerWebExchange addHeader(ServerWebExchange exchange, String key, String value) {
        return exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header(key, value)
                        .build())
                .build();
    }

    public static ServerWebExchange removeHeader(ServerWebExchange exchange, String key) {
        return exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .headers(headers -> headers.remove(key))
                        .build())
                .build();
    }
}
