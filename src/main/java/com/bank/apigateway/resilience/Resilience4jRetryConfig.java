package com.bank.apigateway.resilience;


import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jRetryConfig  {

    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)                 // tối đa retry 3 lần
                .waitDuration(Duration.ofMillis(500)) // giữa các lần retry delay 500ms
                .retryExceptions(
                        java.io.IOException.class,
                        java.util.concurrent.TimeoutException.class
                )
                .ignoreExceptions(
                        IllegalArgumentException.class // không retry với lỗi business
                )
                .build();

        return RetryRegistry.of(config);
    }
}
