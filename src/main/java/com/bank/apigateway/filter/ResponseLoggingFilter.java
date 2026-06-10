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
public class ResponseLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long start = System.currentTimeMillis();

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> logResponse(exchange, start))
                .doOnError(error -> logError(exchange, error, start));
    }

    private void logResponse(ServerWebExchange exchange, long start) {
        int status = exchange.getResponse().getStatusCode() != null
                ? exchange.getResponse().getStatusCode().value()
                : 500;

        long time = System.currentTimeMillis() - start;

        log.info("[GATEWAY-RESP] status={} time={}ms path={}",
                status,
                time,
                exchange.getRequest().getURI().getPath());
    }

    private void logError(ServerWebExchange exchange, Throwable error, long start) {
        long time = System.currentTimeMillis() - start;

        log.error("[GATEWAY-ERROR] path={} time={}ms error={}",
                exchange.getRequest().getURI().getPath(),
                time,
                error.getMessage());
    }

    @Override
    public int getOrder() {
        return -150;
    }
}
