package com.bank.apigateway.resilience;


import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jTimeLimiterConfig  {

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2)) // nếu call > 2s → timeout
                .cancelRunningFuture(true)               // cancel call đang chạy
                .build();

        return TimeLimiterRegistry.of(config);
    }
}
