package com.bank.apigateway.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

@Component
public class SensitiveDataMaskFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(SensitiveDataMaskFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // mask logs for payment endpoints
        if (path.contains("/payments") || path.contains("/transfer")) {
            log.info("[SECURITY] Sensitive endpoint accessed: {}", path);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -50;
    }
}
