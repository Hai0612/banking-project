package com.bank.apigateway;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupCheck {

    private final Tracer tracer;

    @PostConstruct
    public void check() {

        Span span = tracer.nextSpan().name("test");

        try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {

            log.info("TRACE={}", tracer.currentSpan());

        } finally {
            span.end();
        }
    }
}