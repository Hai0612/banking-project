package com.bank.apigateway.exception;


import com.bank.apigateway.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // thread-safe

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        String traceId = MDC.get("trace_id");
        if (traceId == null) traceId = "N/A";

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage() != null ? ex.getMessage() : "Unknown error";

        int statusCode = 500;

        if (ex instanceof ResponseStatusException rse) {
            statusCode = rse.getStatusCode().value();
            message = rse.getReason() != null ? rse.getReason() : message;
        }

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                traceId
        );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (Exception e) {
            bytes = ("{\"status\":500,\"error\":\"Internal Server Error\",\"traceId\":\"" + traceId + "\"}").getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(status);

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}