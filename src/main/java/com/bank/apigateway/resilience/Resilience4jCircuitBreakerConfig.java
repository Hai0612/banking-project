package com.bank.apigateway.resilience;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jCircuitBreakerConfig  {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)       // % request failed -> open
                .slowCallRateThreshold(50)      // % slow calls -> open
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .slowCallDurationThreshold(Duration.ofSeconds(2)) // define slow call
                .permittedNumberOfCallsInHalfOpenState(3)
                .minimumNumberOfCalls(5)        // min calls before CB calculates failure rate
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();

        return CircuitBreakerRegistry.of(defaultConfig);
    }
}
